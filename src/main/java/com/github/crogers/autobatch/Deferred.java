package com.github.crogers.autobatch;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum Deferred {
    ;

    public static <T> DeferredValue<T> value(T value) {
        return new SimpleDeferredValue<>(value);
    }

    public static <T> DeferredValue<T> value(Supplier<T> supplier) {
        return supplier::get;
    }

    public static <A, B, R> DeferredValue<R> combination(DeferredValue<A> a, DeferredValue<B> b, BiFunction<A, B, R> combiner) {
        return new SimpleDeferredValue<>(combiner.apply(a.run(), b.run()));
    }

    public static <A, R> DeferredFunc1<A, R> batch(Class<A> aClass, Class<R> rClass, Batcher<A, R> batcher) {
        return new BatchedDeferredFunc1<>(batcher);
    }
}
