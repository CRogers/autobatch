package com.github.crogers.autobatch;

import com.google.common.base.Suppliers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BatchedDeferredFunc1<A, R> implements DeferredFunc1<A, R> {
    private final List<DeferredValue<A>> invocations = new ArrayList<>();
    private final Supplier<List<R>> savedBatch;

    /* package */ BatchedDeferredFunc1(Batcher<A, R> batcher) {
        this.savedBatch = Suppliers.memoize(() -> {
            List<A> as = invocations.stream()
                    .map(DeferredValue::run)
                    .collect(Collectors.toList());
            return batcher.batch(as);
        });
    }

    @Override
    public DeferredValue<R> apply(A a) {
        return apply(new SimpleDeferredValue<>(a));
    }

    @Override
    public DeferredValue<R> apply(DeferredValue<A> a) {
        int savedLocation = invocations.size();
        invocations.add(a);
        return () -> savedBatch.get().get(savedLocation);
    }
}
