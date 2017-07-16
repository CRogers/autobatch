package uk.callumr.autobatch;

class BatchedDeferredFunc3<A, B, C, R> implements DeferredFunc3<A, B, C, R> {
    private final BatchedFunc<Invocation3<A, B, C>, R> batchedFunc;

    BatchedDeferredFunc3(Batcher<Invocation3<A, B, C>, R> batcher) {
        this.batchedFunc = new BatchedFunc<>(batcher);
    }

    @Override
    public Deferred<R> apply(Deferred<A> a, Deferred<B> b, Deferred<C> c) {
        return batchedFunc.addPending(Invocation3.of(a, b, c));
    }
}
