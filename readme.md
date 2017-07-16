Autobatch
===

Autobatch allows you to write code that will seamlessly batch up requests
by using the same code for individual and bulk operations.
  
Worked example: Deleting resources
---

Pretend we have a resource: posts. We want to do two operations to it:
either delete a single post or delete a whole bunch of posts.
Traditionally, we would have two options:

0. Use the same code for deleting individual posts but just call it
   many times for the bulk delete.
0. Write two pieces of code, one to handle single deletions, one to
   handle bulk deletions.
   
Neither of these are ideal, as 1) will result in a large number of
we requests, degrading performance and 2) needlessly duplicates code.

With Autobatch, we can have our cake an eat it, using the same code
for both use cases and getting maximal performance. The code below
shows what this looks like:

```java
public class Posts {
    private final DeferredFunc1<PostId, Void> deletePost;

    public Posts(PostHttpService postHttpService) {
        this.deletePost = Deferred.batch1Arg(postIdInvocations -> {
            Set<PostId> postsToDelete = new HashSet<>(postIdInvocations);
            postHttpService.deletePosts(postsToDelete);
            return null;
        });
    }

    public Deferred<Void> deletePost(PostId postId) {
        return deletePost.apply(postId);
    }
}
```

Then:

```java
public void singleDelete() {
    Deferred<Void> deferredDeletion = posts.deletePost(postToDelete);
    deferredDeletion.run();
}

public void bulkDelete() {
    Stream<Deferred<Void>> deferredDeletions = postsToDelete.stream()
            .map(posts::deletePost);

    Deferred<Void> allDeletions = Deferred.combineAll(deferredDeletions, voids -> null);
    allDeletions.run();
}
```

In this case `bulkDelete` will only run one batched delete call even
though it has just called `deletePost` multiple times!