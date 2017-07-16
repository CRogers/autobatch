package com.github.crogers.autobatch;

import java.util.function.Function;

public class DeferredFunc1Simplifier<A, R> implements DeferredFunc1<A, R> {
    private final Function<DeferredValue<A>, DeferredValue<R>> function;

    public DeferredFunc1Simplifier(Function<DeferredValue<A>, DeferredValue<R>> function) {
        this.function = function;
    }

    @Override
    public DeferredValue<R> apply(A a) {
        return apply(Deferred.value(a));
    }

    @Override
    public DeferredValue<R> apply(DeferredValue<A> a) {
        return function.apply(a);
    }
}
