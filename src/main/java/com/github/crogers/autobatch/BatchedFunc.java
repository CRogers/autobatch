package com.github.crogers.autobatch;

import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BatchedFunc<T, R> {
    private final Batcher<T, R> batcher;
    private final List<DeferredMemoisedInvocation<T, R>> pendingInvocations = new ArrayList<>();

    /* package */ BatchedFunc(Batcher<T, R> batcher) {
        this.batcher = batcher;
    }

    private void batchIt() {
        List<T> inputs = pendingInvocations.stream()
                .map(DeferredMemoisedInvocation::input)
                .collect(Collectors.toList());

        List<R> results = batcher.batch(inputs);
        StreamEx.of(results)
                .zipWith(pendingInvocations.stream())
                .forKeyValue((result, deferredInvocation) -> {
                    deferredInvocation.setValue(result);
                });

        pendingInvocations.clear();
    }

    public DeferredValue<R> addPending(DeferredValue<T> pendingInvocation) {
        DeferredMemoisedInvocation<T, R> deferredMemoisedInvocation =
                new DeferredMemoisedInvocation<>(pendingInvocation, this::batchIt);
        
        pendingInvocations.add(deferredMemoisedInvocation);
        return deferredMemoisedInvocation;
    }
}
