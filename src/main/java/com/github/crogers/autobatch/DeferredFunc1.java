package com.github.crogers.autobatch;

public interface DeferredFunc1<A, R> {
    DeferredValue<R> apply(A a);
}
