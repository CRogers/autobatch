package uk.callumr.autobatch;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Deferred<T> {
    T run();

    static <T> Deferred<T> value(T value) {
        return new SimpleDeferredValue<>(value);
    }

    static <T> Deferred<T> value(Supplier<T> supplier) {
        return supplier::get;
    }

    static <A, B, R> Deferred<R> combination(Deferred<A> a, Deferred<B> b, BiFunction<A, B, R> combiner) {
        return () -> combiner.apply(a.run(), b.run());
    }

    static <A, B, C, R> Deferred<R> combination(
            Deferred<A> a,
            Deferred<B> b,
            Deferred<C> c,
            Func3<A, B, C, R> combiner) {
        return () -> combiner.apply(a.run(), b.run(), c.run());
    }

    static <T, R> Deferred<R> combineAll(Stream<Deferred<T>> deferredValues, Function<Stream<T>, R> combiner) {
        return () -> combiner.apply(deferredValues.map(Deferred::run));
    }

    static <R> DeferredFunc0<R> batchNoArgs(ArglessBatcher<R> arglessBatcher) {
        return new BatchedDeferredFunc0<>(arglessBatcher);
    }

    static <A, R> DeferredFunc1<A, R> batch1Arg(Batcher<A, R> batcher) {
        return new BatchedDeferredFunc1<>(batcher);
    }

    static <A, B, R> DeferredFunc2<A, B, R> batch2Args(Batcher<Invocation2<A, B>, R> batcher) {
        return new BatchedDeferredFunc2<>(batcher);
    }

    static <A, B, C, R> DeferredFunc3<A, B, C, R> batch3Args(Batcher<Invocation3<A, B, C>, R> batcher) {
        return new BatchedDeferredFunc3<>(batcher);
    }
}
