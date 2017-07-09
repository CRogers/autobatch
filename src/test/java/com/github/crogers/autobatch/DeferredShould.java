package com.github.crogers.autobatch;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeferredShould {
    private final Deferred deferred = new Deferred();

    @Test
    public void deferring_a_value_then_running_it_should_return_the_value() {
        DeferredValue<Integer> deferredValue = deferred.value(10);

        assertThat(deferredValue.run()).isEqualTo(10);
    }

    @Test
    public void combining_two_deferred_values_should_retun_the_combination() {
        DeferredValue<String> hi = deferred.value("hi");
        DeferredValue<Integer> four = deferred.value(4);

        DeferredValue<String> combination = deferred.combination(hi, four, (hi_, four_) -> {
            return hi_ + String.valueOf(four_);
        });

        assertThat(combination.run()).isEqualTo("hi4");
    }
}
