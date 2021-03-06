package uk.callumr.autobatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class BatchedFunc<T, R> {
    private final Batcher<T, R> batcher;
    private final List<DeferredMemoisedInvocation<T, R>> pendingInvocations = new ArrayList<>();

    BatchedFunc(Batcher<T, R> batcher) {
        this.batcher = batcher;
    }

    private void batchIt() {
        List<T> inputs = pendingInvocations.stream()
                .map(DeferredMemoisedInvocation::input)
                .collect(Collectors.toList());

        List<R> results = batcher.batch(inputs);

        for (int i = 0; i < pendingInvocations.size(); i++) {
            DeferredMemoisedInvocation<T, R> deferredMemoisedInvocation = pendingInvocations.get(i);
            R result = results.get(i);
            deferredMemoisedInvocation.setValue(result);
        }

        pendingInvocations.clear();
    }

    public Deferred<R> addPending(Deferred<T> pendingInvocation) {
        DeferredMemoisedInvocation<T, R> deferredMemoisedInvocation =
                new DeferredMemoisedInvocation<>(pendingInvocation, this::batchIt);
        
        pendingInvocations.add(deferredMemoisedInvocation);
        return deferredMemoisedInvocation;
    }
}
