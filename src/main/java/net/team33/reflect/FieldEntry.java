package net.team33.reflect;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class FieldEntry implements Map.Entry<String, Object> {

    private final Object subject;
    private final Map.Entry<String, Field> entry;

    public FieldEntry(final Map.Entry<String, Field> entry, final Object subject) {
        this.entry = entry;
        this.subject = subject;
    }

    @Override
    public final String getKey() {
        return entry.getKey();
    }

    @Override
    public final Object getValue() {
        try {
            return entry.getValue().get(subject);
        } catch (final IllegalAccessException caught) {
            throw new IllegalStateException("cannot access field [" + entry.getKey() + "]", caught);
        }
    }

    @Override
    public final Object setValue(final Object value) {
        throw new UnsupportedOperationException("not modifiable this way");
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Map.Entry) && matches((Map.Entry<?, ?>) obj));
    }

    private boolean matches(final Map.Entry<?, ?> other) {
        return Objects.equals(getKey(), other.getKey()) && Objects.equals(getValue(), other.getValue());
    }

    @Override
    public final String toString() {
        return getKey() + "=" + getValue();
    }
}
