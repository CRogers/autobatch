package com.github.crogers.autobatch;

public interface DeferredFunc2<A, B, R> {
    default DeferredValue<R> apply(A a, B b) {
        return apply(Deferred.value(a), Deferred.value(b));
    }

    default DeferredValue<R> apply(DeferredValue<A> a, B b) {
        return apply(a, Deferred.value(b));
    }

    default DeferredValue<R> apply(A a, DeferredValue<B> b) {
        return apply(Deferred.value(a), b);
    }

    DeferredValue<R> apply(DeferredValue<A> a, DeferredValue<B> b);
}
