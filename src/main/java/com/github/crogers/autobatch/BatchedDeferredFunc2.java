package com.github.crogers.autobatch;

import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BatchedDeferredFunc2<A, B, R> implements DeferredFunc2<A, B, R> {
    private final Batcher<Invocation2<A, B>, R> batcher;
    private final List<DeferredMemoisedInvocation<Invocation2<A, B>, R>> pendingInvocations = new ArrayList<>();

    /* package */ BatchedDeferredFunc2(Batcher<Invocation2<A, B>, R> batcher) {
        this.batcher = batcher;
    }

    private void batchIt() {
        List<Invocation2<A, B>> inputs = pendingInvocations.stream()
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
    public DeferredValue<R> apply(DeferredValue<A> a, DeferredValue<B> b) {
        DeferredValue<Invocation2<A, B>> invocation2 = Invocation2.of(a, b);
        DeferredMemoisedInvocation<Invocation2<A, B>, R> deferredMemoisedInvocation =
                new DeferredMemoisedInvocation<>(invocation2, this::batchIt);

        pendingInvocations.add(deferredMemoisedInvocation);
        return deferredMemoisedInvocation;
    }
}
