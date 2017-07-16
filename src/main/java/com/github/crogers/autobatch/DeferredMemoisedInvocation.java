package com.github.crogers.autobatch;

import java.util.Objects;

class DeferredMemoisedInvocation<A, R> implements Deferred<R> {
    private final Deferred<A> deferredValue;
    private final Runnable invoker;
    private R value;

    DeferredMemoisedInvocation(Deferred<A> deferredValue, Runnable invoker) {
        this.deferredValue = deferredValue;
        this.invoker = invoker;
    }

    @Override
    public R run() {
        if (value == null) {
            invoker.run();
        }
        return Objects.requireNonNull(value);
    }

    public A input() {
        return deferredValue.run();
    }

    public void setValue(R value) {
        this.value = value;
    }
}
