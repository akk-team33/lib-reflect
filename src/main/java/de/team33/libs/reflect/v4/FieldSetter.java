package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

import static java.lang.String.format;

class FieldSetter<T> implements BiConsumer<T, Object> {

    private static final String FIELD_NOT_FOUND = "no field \"%s\" found in class <%s>";
    private static final String ILLEGAL_VALUE = "cannot set field <%s> to value <%s>";

    private final Field field;

    FieldSetter(final Class<T> type, final String fieldName) {
        try {
            this.field = accessible(type.getField(fieldName));
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException(format(FIELD_NOT_FOUND, fieldName, type.getCanonicalName()), e);
        }
    }

    private static Field accessible(final Field field) {
        field.setAccessible(true);
        return field;
    }

    @Override
    public void accept(final T subject, final Object value) {
        try {
            field.set(subject, value);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(format(ILLEGAL_VALUE, field, value), e);
        }
    }
}
