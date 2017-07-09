package com.github.crogers.autobatch;

public class Deferred {

    public <T> DeferredValue<T> value(T value) {
        return new DeferredValue<>(value);
    }
}
