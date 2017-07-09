package com.github.crogers.autobatch;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeferredShould {
    @Test
    public void deferring_a_value_then_running_it_should_return_the_value() {
        Deferred deferred = new Deferred();
        DeferredValue<Integer> deferredValue = deferred.value(10);

        assertThat(deferredValue.run()).isEqualTo(10);
    }
}
