package com.github.crogers.autobatch;

public interface DeferredFunc2<A, B, R> {
    default Deferred<R> apply(A a, B b) {
        return apply(Deferred.value(a), Deferred.value(b));
    }

    default Deferred<R> apply(Deferred<A> a, B b) {
        return apply(a, Deferred.value(b));
    }

    default Deferred<R> apply(A a, Deferred<B> b) {
        return apply(Deferred.value(a), b);
    }

    Deferred<R> apply(Deferred<A> a, Deferred<B> b);
}
