package uk.callumr.autobatch;

import org.immutables.value.Value;

@Value.Immutable
public interface Invocation2<A, B> {
    A first();
    B second();

    static <A, B> Deferred<Invocation2<A, B>> of(Deferred<A> first, Deferred<B> second) {
        return Deferred.combination(first, second, Invocation2::of);
    }

    static <A, B> Invocation2<A, B> of(A first, B second) {
        return ImmutableInvocation2.<A, B>builder()
                .first(first)
                .second(second)
                .build();
    }
}
