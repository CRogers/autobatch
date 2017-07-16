package uk.callumr.autobatch;

class SimpleDeferredValue<T> implements Deferred<T> {
    private final T value;

    SimpleDeferredValue(T value) {
        this.value = value;
    }

    @Override
    public T run() {
        return value;
    }
}
