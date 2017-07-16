package com.github.crogers.autobatch;

public class BatchedDeferredFunc3<A, B, C, R> implements DeferredFunc3<A, B, C, R> {
    private final BatchedFunc<Invocation3<A, B, C>, R> batchedFunc;

    public BatchedDeferredFunc3(Batcher<Invocation3<A, B, C>, R> batcher) {
        this.batchedFunc = new BatchedFunc<>(batcher);
    }

    @Override
    public DeferredValue<R> apply(DeferredValue<A> a, DeferredValue<B> b, DeferredValue<C> c) {
        return batchedFunc.addPending(Invocation3.of(a, b, c));
    }
}
