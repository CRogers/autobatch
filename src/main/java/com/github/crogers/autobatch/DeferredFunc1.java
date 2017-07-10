package com.github.crogers.autobatch;

public interface DeferredFunc1<A, R> {
    DeferredValue<R> apply(A a);
    DeferredValue<R> apply(DeferredValue<A> a);
}
