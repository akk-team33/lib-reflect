package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;

public class FieldMapper<T> {

    public static final Factory FACTORY = factory(Fields.Mapping.SIGNIFICANT_DEEP);

    private static final String CANNOT_GET_FIELD = "cannot get value of field <%s> of instance <%s>";
    private static final String CANNOT_SET_FIELD = "cannot Set field <%s> of instance <%s> to value <%s>";

    private final Map<String, Field> mapping;

    private FieldMapper(final Map<String, Field> mapping) {
        this.mapping = mapping;
    }

    public static Factory factory(final Function<Class<?>, Map<String, Field>> mapping) {
        return new Factory() {
            @Override
            public <T> FieldMapper<T> apply(final Class<T> subjectClass) {
                return new FieldMapper<T>(mapping.apply(subjectClass));
            }
        };
    }

    private static void set(final Field field, final Object target, final Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(format(CANNOT_SET_FIELD, field, target, value), e);
        }
    }

    private static Object get(final Field field, final Object origin) {
        try {
            return field.get(origin);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(format(CANNOT_GET_FIELD, field, origin), e);
        }
    }

    public final T copy(final T origin, final T target) {
        mapping.forEach((name, field) -> set(field, target, get(field, origin)));
        return target;
    }

    public final <M extends Map<String, Object>> M map(final T origin, final M target) {
        mapping.forEach((name, field) -> target.put(name, get(field, origin)));
        return target;
    }

    public final T map(final Map<?, ?> origin, final T target) {
        mapping.forEach((name, field) -> set(field, target, origin.get(name)));
        return target;
    }

    public interface Factory {

        <T> FieldMapper<T> apply(Class<T> subjectClass);
    }
}
