package com.github.crogers.autobatch;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class DeferredShould {

    @Test
    public void deferring_a_value_then_running_it_should_return_the_value() {
        Deferred<Integer> deferredValue = Deferred.value(10);

        assertThat(deferredValue.run()).isEqualTo(10);
    }

    @Test
    public void deferring_a_supplied_value_should_return_it_and_only_calculate_it_on_when_run() {
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(4);

        Deferred<Integer> deferredValue = Deferred.value(supplier);
        verifyZeroInteractions(supplier);

        assertThat(deferredValue.run()).isEqualTo(4);
    }

    @Test
    public void combining_two_deferred_values_should_return_the_combination_and_only_compute_it_when_run() {
        Deferred<String> hi = deferredValueMock("hi");
        Deferred<Integer> four = deferredValueMock(4);

        BiFunction<String, Integer, String> combiner = mock(BiFunction.class);
        when(combiner.apply(anyString(), anyInt())).thenAnswer(invocation -> {
            String string = invocation.getArgument(0);
            int integer = invocation.getArgument(1);
            return string + String.valueOf(integer);
        });

        Deferred<String> combination = Deferred.combination(hi, four, combiner);
        verifyZeroInteractions(combiner, hi, four);

        assertThat(combination.run()).isEqualTo("hi4");
    }

    private <T> Deferred<T> deferredValueMock(T value) {
        Deferred<T> deferredValue = mock(Deferred.class);
        when(deferredValue.run()).thenReturn(value);
        return deferredValue;
    }

    @Test
    public void combination_of_all_should_combine_multiple_values_and_only_compute_when_run() {
        Function<Stream<Integer>, Integer> combiner = mock(Function.class);
        when(combiner.apply(any())).thenAnswer(invocation -> {
            Stream<Integer> ints = invocation.getArgument(0);
            return ints.mapToInt(i -> i).sum();
        });

        List<Deferred<Integer>> values = ImmutableList.of(
                deferredValueMock(1), deferredValueMock(2), deferredValueMock(3));

        Deferred<Integer> sum = Deferred.combinationOfAll(values.stream(), combiner);
        verifyZeroInteractions(combiner);
        verifyZeroInteractions(values.toArray());

        assertThat(sum.run()).isEqualTo(6);
    }

    @Test
    public void a_batched_value_should_batch_its_calls_if_called_twice() {
        Batcher<Character, Integer> batcher = mock(Batcher.class);
        when(batcher.batch(anyList())).thenReturn(ImmutableList.of(1, 2));

        DeferredFunc1<Character, Integer> batchedFunc = Deferred.batch1Arg(batcher);

        Deferred<Integer> result1 = batchedFunc.apply('1');
        Deferred<Integer> result2 = batchedFunc.apply('2');

        assertThat(result1.run()).isEqualTo(1);
        assertThat(result2.run()).isEqualTo(2);

        verify(batcher).batch(ImmutableList.of('1', '2'));
        verifyNoMoreInteractions(batcher);
    }

    @Test
    public void a_value_depending_on_another() {
        DeferredFunc1<Character, Integer> func1 = Deferred.batch1Arg(chars ->
                chars.stream().map(c -> (int) c).collect(Collectors.toList()));

        Deferred<Integer> a = func1.apply('a');

        DeferredFunc1<Integer, Long> func2 = Deferred.batch1Arg(ints ->
                ints.stream().map(i -> (long) i).collect(Collectors.toList()));

        Deferred<Long> b = func2.apply(a);

        assertThat(b.run()).isEqualTo(97);
    }

    @Test
    public void work_when_calling_run_multiple_times_with_different_args() {
        DeferredFunc1<Boolean, Boolean> func = Deferred.batch1Arg(bools ->
            bools.stream().map(b -> !b).collect(Collectors.toList())
        );

        Deferred<Boolean> first = func.apply(true);
        first.run();

        Deferred<Boolean> second = func.apply(false);
        assertThat(second.run()).isTrue();
    }

    private <T, R> Batcher<T, R> batcherFor(Batcher<T, R> batcher) {
        Batcher<T, R> mockedBatcher = mock(Batcher.class);
        when(mockedBatcher.batch(anyList())).then(invocation -> batcher.batch(invocation.getArgument(0)));
        return mockedBatcher;
    }
}
