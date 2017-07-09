package com.github.crogers.autobatch;

import java.util.function.BiFunction;

public class Deferred {

    public <T> DeferredValue<T> value(T value) {
        return new DeferredValue<>(value);
    }

    public <A, B, R> DeferredValue<R> combination(DeferredValue<A> a, DeferredValue<B> b, BiFunction<A, B, R> combiner) {
        return new DeferredValue<>(combiner.apply(a.run(), b.run()));
    }
}
