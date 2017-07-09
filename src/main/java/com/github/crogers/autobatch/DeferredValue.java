package com.github.crogers.autobatch;

public class DeferredValue<T> {
    private final T value;

    public DeferredValue(T value) {
        this.value = value;
    }

    public T run() {
        return value;
    }
}
