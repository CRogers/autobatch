package blah;

public enum Funcs {
    ;

    interface Func0<R> {
        R apply();
    }

    interface Func1<A, R> {
        R apply(A a);
    }

    interface Func2<A, B, R> {
        R apply(A a, B b);
    }

    interface Func3<A, B, C, R> {
        R apply(A a, B b, C c);
    }

}
