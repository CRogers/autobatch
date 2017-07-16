package uk.callumr.autobatch;

public interface DeferredFunc1<A, R> {
    default Deferred<R> apply(A a) {
        return apply(Deferred.value(a));
    }

    Deferred<R> apply(Deferred<A> a);
}
