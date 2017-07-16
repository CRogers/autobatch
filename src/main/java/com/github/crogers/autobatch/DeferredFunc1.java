package com.github.crogers.autobatch;

public interface DeferredFunc1<A, R> {
    default DeferredValue<R> apply(A a) {
        return apply(Deferred.value(a));
    }

    DeferredValue<R> apply(DeferredValue<A> a);
}
