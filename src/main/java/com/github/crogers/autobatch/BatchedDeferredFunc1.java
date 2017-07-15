package com.github.crogers.autobatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BatchedDeferredFunc1<A, R> implements DeferredFunc1<A, R> {
    private final Batcher<A, R> batcher;
    private final List<DeferredValue<A>> pendingInvocations = new ArrayList<>();
    private final List<R> savedValues = new ArrayList<>();

    /* package */ BatchedDeferredFunc1(Batcher<A, R> batcher) {
        this.batcher = batcher;
    }

    @Override
    public DeferredValue<R> apply(A a) {
        return apply(new SimpleDeferredValue<>(a));
    }

    @Override
    public DeferredValue<R> apply(DeferredValue<A> a) {
        int savedLocation = savedValues.size() + pendingInvocations.size();
        pendingInvocations.add(a);
        return () -> {
            if (savedLocation >= savedValues.size()) {
                List<A> invocations = pendingInvocations.stream()
                        .map(DeferredValue::run)
                        .collect(Collectors.toList());

                List<R> results = batcher.batch(invocations);
                savedValues.addAll(results);
            }

            return savedValues.get(savedLocation);
        };
    }
}
