package de.team33.libs.reflect.v4;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Property<T> {

    static <T> Builder<T> builder() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    static <T, V> Property<T> setup(String name, Function<T, V> getter, BiConsumer<T, V> setter) {
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

    class Builder<T> {

        public final Property<T> build() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public final Builder<T> setGetter(final Function<T, Object> getter) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public final <V> Builder<T> setSetter(final BiConsumer<T, V> setter) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
