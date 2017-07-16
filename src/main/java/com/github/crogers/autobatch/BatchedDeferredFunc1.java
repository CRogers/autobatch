package com.github.crogers.autobatch;

import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BatchedDeferredFunc1<A, R> implements DeferredFunc1<A, R> {
    private final Batcher<A, R> batcher;
    private final List<DeferredMemoisedInvocation<A, R>> pendingInvocations = new ArrayList<>();

    /* package */ BatchedDeferredFunc1(Batcher<A, R> batcher) {
        this.batcher = batcher;
    }

    private void batchIt() {
        List<A> inputs = pendingInvocations.stream()
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

    @Override
    public DeferredValue<R> apply(DeferredValue<A> a) {
        DeferredMemoisedInvocation<A, R> deferredMemoisedInvocation = new DeferredMemoisedInvocation<>(a, this::batchIt);
        pendingInvocations.add(deferredMemoisedInvocation);
        return deferredMemoisedInvocation;
    }
}
