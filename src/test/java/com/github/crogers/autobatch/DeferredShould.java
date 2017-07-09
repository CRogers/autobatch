package com.github.crogers.autobatch;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

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

    @Test
    public void a_batched_value_should_batch_its_calls_if_called_twice() {
        Batcher<Character, Integer> batcher = mock(Batcher.class);
        when(batcher.batch(anyList())).thenReturn(ImmutableList.of(1, 2));

        DeferredFunc1<Character, Integer> batchedFunc = deferred.batch(Character.class, Integer.class, batcher);

        DeferredValue<Integer> result1 = batchedFunc.apply('1');
        DeferredValue<Integer> result2 = batchedFunc.apply('2');

        DeferredValue<String> bothResults = deferred.combination(result1, result2, (a, b) -> String.format("%d:%d", a, b));

        assertThat(bothResults.run()).isEqualTo("1:2");

        verify(batcher).batch(ImmutableList.of('1', '2'));
        verifyNoMoreInteractions(batcher);
    }
}
