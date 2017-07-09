package com.github.crogers.autobatch;

import com.google.common.base.Suppliers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BatchedDeferredFunc1<A, R> implements DeferredFunc1<A, R> {
    private final List<A> invocations = new ArrayList<>();
    private final Supplier<List<R>> savedBatch;

    public BatchedDeferredFunc1(Batcher<A, R> batcher) {
        this.savedBatch = Suppliers.memoize(() ->
                batcher.batch(invocations)
        );
    }

    @Override
    public DeferredValue<R> apply(A a) {
        int savedLocation = invocations.size();
        invocations.add(a);
        return () -> savedBatch.get().get(savedLocation);
    }
}
