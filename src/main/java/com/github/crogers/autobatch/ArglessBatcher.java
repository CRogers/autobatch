package com.github.crogers.autobatch;

import java.util.List;

public interface ArglessBatcher<R> {
    List<R> batch(int timesCalled);
}
