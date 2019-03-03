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
    FLAT(Fields::flat),

    /**
     * Delivers all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    DEEP(Fields::deep);

    private static Stream<Field> deep(final Class<?> aClass) {
        return (null == aClass)
                ? Stream.empty()
                : Stream.concat(deep(aClass.getSuperclass()), flat(aClass));
    }

    private static Stream<Field> flat(final Class<?> aClass) {
        return Stream.of(aClass.getDeclaredFields());
    }

    private final Function<Class<?>, Stream<Field>> backing;

    Fields(final Function<Class<?>, Stream<Field>> backing) {
        this.backing = backing;
    }

    @Override
    public final Stream<Field> apply(final Class<?> subject) {
        return backing.apply(subject);
    }
}
