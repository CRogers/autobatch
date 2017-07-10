package com.github.crogers.autobatch;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.stream.Collectors;

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

        assertThat(result1.run()).isEqualTo(1);
        assertThat(result2.run()).isEqualTo(2);

        verify(batcher).batch(ImmutableList.of('1', '2'));
        verifyNoMoreInteractions(batcher);
    }

    @Test
    public void a_value_depending_on_another() {
        DeferredFunc1<Character, Integer> func1 = deferred.batch(Character.class, Integer.class, chars -> chars.stream().map(c -> (int) c).collect(Collectors.toList()));
        DeferredValue<Integer> a = func1.apply('a');

        DeferredFunc1<Integer, Long> func2 = deferred.batch(Integer.class, Long.class, ints -> ints.stream().map(i -> (long) i).collect(Collectors.toList()));
        DeferredValue<Long> b = func2.apply(a);

        assertThat(b.run()).isEqualTo(97);
    }
}
