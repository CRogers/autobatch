package com.github.crogers.autobatch;

public class BatchedDeferredFunc2<A, B, R> implements DeferredFunc2<A, B, R> {
    private final BatchedFunc<Invocation2<A, B>, R> batchedFunc;

    public BatchedDeferredFunc2(Batcher<Invocation2<A, B>, R> batcher) {
        this.batchedFunc = new BatchedFunc<>(batcher);
    }

    @Override
    public Deferred<R> apply(Deferred<A> a, Deferred<B> b) {
        return batchedFunc.addPending(Invocation2.of(a, b));
    }
}
