package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;

public class FieldProperty implements Property {

    private final Class<?> context;
    private final Field field;

    public FieldProperty(final Class<?> context, final Field field) {
        this.context = context;
        this.field = field;
    }

    @Override
    public String getName() {
        return field.getName();
    }
}
