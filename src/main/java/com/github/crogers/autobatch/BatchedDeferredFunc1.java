package com.github.crogers.autobatch;

public class BatchedDeferredFunc1<A, R> implements DeferredFunc1<A, R> {
    private final BatchedFunc<A, R> batchedFunc;

    public BatchedDeferredFunc1(Batcher<A, R> batcher) {
        this.batchedFunc = new BatchedFunc<>(batcher);
    }

    @Override
    public Deferred<R> apply(Deferred<A> a) {
        return batchedFunc.addPending(a);
    }
}
