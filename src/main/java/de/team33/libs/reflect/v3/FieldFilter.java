package de.team33.libs.reflect.v3;

import de.team33.libs.reflect.v3.Modifiers;

import java.lang.reflect.Field;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Provides some predefined {@linkplain Predicate filters} for {@link Field Fields}.
 */
public enum FieldFilter implements Predicate<Field> {

    /**
     * Defines a filter accepting all fields (including static fields).
     */
    ANY(Modifiers.Predicate.TRUE),

    /**
     * Defines a filter accepting all public fields.
     */
    PUBLIC(Modifiers.Predicate.PUBLIC),

    /**
     * Defines a filter accepting all private fields.
     */
    PRIVATE(Modifiers.Predicate.PRIVATE),

    /**
     * Defines a filter accepting all protected fields.
     */
    PROTECTED(Modifiers.Predicate.PROTECTED),

    /**
     * Defines a filter accepting all static fields.
     */
    STATIC(Modifiers.Predicate.STATIC),

    /**
     * Defines a filter accepting all final fields.
     */
    FINAL(Modifiers.Predicate.FINAL),

    /**
     * Defines a filter accepting all transient fields.
     */
    TRANSIENT(Modifiers.Predicate.TRANSIENT),

    /**
     * Defines a filter accepting all instance-fields (non-static fields).
     */
    INSTANCE(Modifiers.Predicate.STATIC.negate()),

    /**
     * Defines a filter accepting all but static or transient fields.
     * Those fields should be significant for a type with value semantics.
     */
    SIGNIFICANT(Modifiers.Predicate.STATIC.or(Modifiers.Predicate.TRANSIENT).negate());

    private final IntPredicate filter;

    FieldFilter(final IntPredicate filter) {
        this.filter = filter;
    }

    @Override
    public final boolean test(final Field field) {
        return filter.test(field.getModifiers());
    }
}
