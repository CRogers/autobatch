package uk.callumr.autobatch;

class BatchedDeferredFunc0<R> implements DeferredFunc0<R> {
    private final BatchedFunc<NoInformation, R> batchedFunc;

    BatchedDeferredFunc0(ArglessBatcher<R> arglessBatcher) {
        this.batchedFunc = new BatchedFunc<>(invocations -> arglessBatcher.batch(invocations.size()));
    }

    @Override
    public Deferred<R> apply() {
        return batchedFunc.addPending(Deferred.value(NoInformation.NO_INFORMATION));
    }
}
