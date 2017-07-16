package com.github.crogers.autobatch;

import com.google.common.collect.Iterables;

import java.util.function.BiFunction;
import java.util.function.Function;
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
        return () -> combiner.apply(a.run(), b.run());
    }

    public static <T, R> DeferredValue<R> combinationOfAll(Iterable<DeferredValue<T>> deferredValues, Function<Iterable<T>, R> combiner) {
        return () -> combiner.apply(Iterables.transform(deferredValues, DeferredValue::run));
    }

    public static <A, R> DeferredFunc1<A, R> batch(Class<A> aClass, Class<R> rClass, Batcher<A, R> batcher) {
        return new BatchedDeferredFunc1<>(batcher);
    }

    public static <A, B, R> DeferredFunc2<A, B, R> batch(Class<A> aClass, Class<B> bClass, Class<R> rClass, Batcher<Invocation2<A, B>, R> batcher) {
        return new BatchedDeferredFunc2<>(batcher);
    }
}
