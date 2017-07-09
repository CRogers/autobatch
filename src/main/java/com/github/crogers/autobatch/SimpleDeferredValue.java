package com.github.crogers.autobatch;

public class SimpleDeferredValue<T> implements DeferredValue<T> {
    private final T value;

    /* package */ SimpleDeferredValue(T value) {
        this.value = value;
    }

    @Override
    public T run() {
        return value;
    }
}
