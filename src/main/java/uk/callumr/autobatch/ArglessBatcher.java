package uk.callumr.autobatch;

import java.util.List;

public interface ArglessBatcher<R> {
    List<R> batch(int timesCalled);
}
