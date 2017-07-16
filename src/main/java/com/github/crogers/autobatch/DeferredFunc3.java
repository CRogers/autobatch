package com.github.crogers.autobatch;

public interface DeferredFunc3<A, B, C, R> {
    default Deferred<R> apply(A a, B b, C c) {
        return apply(Deferred.value(a), Deferred.value(b), Deferred.value(c));
    }

    default Deferred<R> apply(A a, B b, Deferred<C> c) {
        return apply(Deferred.value(a), Deferred.value(b), c);
    }

    default Deferred<R> apply(A a, Deferred<B> b, C c) {
        return apply(Deferred.value(a), b, Deferred.value(c));
    }

    default Deferred<R> apply(A a, Deferred<B> b, Deferred<C> c) {
        return apply(Deferred.value(a), b, c);
    }

    default Deferred<R> apply(Deferred<A> a, B b, C c) {
        return apply(a, Deferred.value(b), Deferred.value(c));
    }

    default Deferred<R> apply(Deferred<A> a, B b, Deferred<C> c) {
        return apply(a, Deferred.value(b), c);
    }

    default Deferred<R> apply(Deferred<A> a, Deferred<B> b, C c) {
        return apply(a, b, Deferred.value(c));
    }

    Deferred<R> apply(Deferred<A> a, Deferred<B> b, Deferred<C> c);
}
