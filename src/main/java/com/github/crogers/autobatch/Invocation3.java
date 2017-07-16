package com.github.crogers.autobatch;

import org.immutables.value.Value;

@Value.Immutable
public interface Invocation3<A, B, C> {
    A first();
    B second();
    C third();

    static <A, B, C> Deferred<Invocation3<A, B, C>> of(
            Deferred<A> first,
            Deferred<B> second,
            Deferred<C> third) {

        return Deferred.combination(first, second, third, Invocation3::of);
    }

    static <A, B, C> Invocation3<A, B, C> of(A first, B second, C third) {
        return ImmutableInvocation3.<A, B, C>builder()
                .first(first)
                .second(second)
                .third(third)
                .build();
    }
}
