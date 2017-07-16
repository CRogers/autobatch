package com.github.crogers.autobatch;

public interface DeferredFunc3<A, B, C, R> {
    default DeferredValue<R> apply(A a, B b, C c) {
        return apply(Deferred.value(a), Deferred.value(b), Deferred.value(c));
    }

    default DeferredValue<R> apply(A a, B b, DeferredValue<C> c) {
        return apply(Deferred.value(a), Deferred.value(b), c);
    }

    default DeferredValue<R> apply(A a, DeferredValue<B> b, C c) {
        return apply(Deferred.value(a), b, Deferred.value(c));
    }

    default DeferredValue<R> apply(A a, DeferredValue<B> b, DeferredValue<C> c) {
        return apply(Deferred.value(a), b, c);
    }

    default DeferredValue<R> apply(DeferredValue<A> a, B b, C c) {
        return apply(a, Deferred.value(b), Deferred.value(c));
    }

    default DeferredValue<R> apply(DeferredValue<A> a, B b, DeferredValue<C> c) {
        return apply(a, Deferred.value(b), c);
    }

    default DeferredValue<R> apply(DeferredValue<A> a, DeferredValue<B> b, C c) {
        return apply(a, b, Deferred.value(c));
    }

    DeferredValue<R> apply(DeferredValue<A> a, DeferredValue<B> b, DeferredValue<C> c);
}
