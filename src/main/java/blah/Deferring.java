package blah;

import blah.Funcs.Func0;
import blah.Funcs.Func2;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Function;

public enum Deferring {
    ;

    interface Invocation2<A, B> {
        A a();
        B b();
    }

    interface Batcher<I, R> {
        List<R> batch(List<I> invocations);
    }

    interface Deferred {
        <T> DeferredValue<T> value(T value);
        <A, B, R> DeferredValue<R> combination(DeferredValue<A> a, DeferredValue<B> b, Func2<A, B, R> combiner);
        <A, R> DeferredValue<R> combineMany(Iterable<DeferredValue<A>> values, Function<Iterable<A>, R> combiner);

        <A, B, R> DeferredFunc2<A, B, R> batch(Class<A> aClass, Class<B> bClass, Class<R> rClass, Batcher<Invocation2<A, B>, R> batcher);

        <R> DeferredFunc0<R> callThroughTo(Func0<R> func);
    }

    interface DeferredValue<T> {
        <B> DeferredValue<B> map(Function<T, B> func);
    }

    interface DeferredFunc0<R> {
        DeferredValue<R> call();
    }

    interface DeferredFunc1<A, R> {
        DeferredValue<R> withDeferredArg(DeferredValue<A> a);
        DeferredValue<R> apply(A a);
    }

    interface DeferredFunc2<A, B, R> {
        DeferredValue<R> apply(DeferredValue<A> a, DeferredValue<B> b);
        DeferredValue<R> apply(DeferredValue<A> a, B b);
        DeferredValue<R> apply(A a, DeferredValue<B> b);
        DeferredValue<R> apply(A a, B b);

        DeferredFunc1<B, R> apply(DeferredValue<A> a);
    }

    interface Namer {
        long idFor(String name);
    }

    interface DeferredNamer {
        DeferredFunc1<String, Long> idForName();
    }

    interface Permissions {
        boolean hasPermission(long id, String user);
        boolean pingPong();
    }

    static class DeferredPermissions {
        private final DeferredFunc2<Long, String, Boolean> hasPermission;
        private final DeferredFunc0<Boolean> pingPong;

        public DeferredPermissions(Deferred deferred, Permissions permissions) {
            this.hasPermission = deferred.batch(Long.class, String.class, Boolean.class, (invocations) -> {
                return ImmutableList.of();
            });
            this.pingPong = deferred.callThroughTo(permissions::pingPong);
        }

        public DeferredFunc2<Long, String, Boolean> hasPermission() {
            return hasPermission;
        }

        public DeferredFunc0<Boolean> pingPong() {
            return pingPong;
        }
    }

    static void test(Deferred deferred, DeferredPermissions permissions, DeferredNamer namer) {
        String someName = "name";
        String someUser = "user";

        DeferredValue<Long> id = namer
                .idForName().apply(someName);

        DeferredValue<Boolean> hasPermission = permissions
                .hasPermission().apply(id, someUser);

        DeferredValue<Boolean> alive = permissions.pingPong().call();

        DeferredValue<Boolean> aliveAndPingingWithPerms = deferred.combination(alive, hasPermission, Boolean::logicalAnd);
    }

    static class DeferredValueImpl<T> implements DeferredValue<T> {
        private final T value;

        public DeferredValueImpl(T value) {
            this.value = value;
        }

        @Override
        public <B> DeferredValue<B> map(Function<T, B> func) {
            return new DeferredValueImpl<>(func.apply(value));
        }
    }

    static class DeferredImpl implements Deferred {

        @Override
        public <T> DeferredValue<T> value(T value) {
            return null;
        }

        @Override
        public <A, B, R> DeferredValue<R> combination(DeferredValue<A> a, DeferredValue<B> b, Func2<A, B, R> combiner) {
            return null;
        }

        @Override
        public <A, R> DeferredValue<R> combineMany(Iterable<DeferredValue<A>> deferredValues, Function<Iterable<A>, R> combiner) {
            return null;
        }
    }
}
