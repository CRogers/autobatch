package uk.callumr.autobatch;

import java.util.List;

public interface Batcher<I, R> {
    List<R> batch(List<I> invocations);
}
