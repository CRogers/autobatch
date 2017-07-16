package uk.callumr.autobatch;

public interface DeferredFunc0<R> {
    Deferred<R> apply();
}
