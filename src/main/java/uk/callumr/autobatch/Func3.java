package uk.callumr.autobatch;

@FunctionalInterface
public interface Func3<A, B, C, R> {
    R apply(A a, B b, C c);
}
