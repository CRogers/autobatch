package uk.callumr.autobatch;

class DeferredMemoisedInvocation<A, R> implements Deferred<R> {
    private final Deferred<A> deferredValue;
    private final Runnable invoker;
    private R value;
    private boolean valueHasBeenSet = false;

    DeferredMemoisedInvocation(Deferred<A> deferredValue, Runnable invoker) {
        this.deferredValue = deferredValue;
        this.invoker = invoker;
    }

    @Override
    public R run() {
        if (!valueHasBeenSet) {
            invoker.run();
        }
        return value;
    }

    public A input() {
        return deferredValue.run();
    }

    public void setValue(R value) {
        this.value = value;
        this.valueHasBeenSet = true;
    }
}
