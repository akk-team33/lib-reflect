package de.team33.libs.reflect.v4;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Property<T> {

    static <T, V> Property<T> simple(String name, Function<T, V> getter, BiConsumer<T, V> setter) {
        return new Property<T>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Object get(final T subject) {
                return getter.apply(subject);
            }

            @Override
            public void set(final T target, final Object value) {
                //noinspection unchecked
                setter.accept(target, (V) value);
            }
        };
    }

    String getName();

    Object get(T subject);

    void set(T target, Object value) throws ClassCastException, IllegalArgumentException;
}
