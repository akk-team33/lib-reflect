package de.team33.libs.reflect.v3;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Provides {@link Function methods} to get a {@link Stream} of {@link Field Fields} from a given {@link Class}.
 */
public enum Fields implements Function<Class<?>, Stream<Field>> {

    /**
     * Delivers all {@link Field}s straightly declared by a given {@link Class}
     */
    FLAT {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return Stream.of(subject.getDeclaredFields());
        }
    },

    /**
     * Delivers all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    DEEP {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return (null == subject)
                    ? Stream.empty()
                    : Stream.concat(apply(subject.getSuperclass()), Stream.of(subject.getDeclaredFields()));
        }
    }
}
