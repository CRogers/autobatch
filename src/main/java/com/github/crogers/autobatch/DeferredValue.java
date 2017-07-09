package com.github.crogers.autobatch;

public interface DeferredValue<T> {
    T run();
}
