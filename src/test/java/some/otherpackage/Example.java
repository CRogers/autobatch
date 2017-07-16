package some.otherpackage;

import uk.callumr.autobatch.Deferred;
import uk.callumr.autobatch.DeferredFunc1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Example {
    interface PostId {}

    interface PostHttpService {
        void deletePosts(Set<PostId> postId);
    }


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

    public class SomeClass {
        private Posts posts;

        public void singleDelete() {
            PostId postToDelete = null;
            Deferred<Void> deferredDeletion = posts.deletePost(postToDelete);
            deferredDeletion.run();
        }

        public void bulkDelete() {
            List<PostId> postsToDelete = null;

            Stream<Deferred<Void>> deferredDeletions = postsToDelete.stream()
                    .map(posts::deletePost);

            Deferred<Void> allDeletions = Deferred.combineAll(deferredDeletions, voids -> null);
            allDeletions.run();
        }
    }
}
