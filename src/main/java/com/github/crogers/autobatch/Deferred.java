package com.github.crogers.autobatch;

import java.util.function.BiFunction;

public class Deferred {

    public <T> DeferredValue<T> value(T value) {
        return new SimpleDeferredValue<>(value);
    }

    public <A, B, R> DeferredValue<R> combination(DeferredValue<A> a, DeferredValue<B> b, BiFunction<A, B, R> combiner) {
        return new SimpleDeferredValue<>(combiner.apply(a.run(), b.run()));
    }

    public <A, R> DeferredFunc1<A, R> batch(Class<A> aClass, Class<R> rClass, Batcher<A, R> batcher) {
        return new BatchedDeferredFunc1<>(batcher);
    }
}
