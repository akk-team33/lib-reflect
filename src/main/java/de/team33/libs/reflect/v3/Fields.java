package de.team33.libs.reflect.v3;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Utility for dealing with fields.
 */
public class Fields {

    /**
     * Streams all {@link Field}s straightly declared by a given {@link Class}
     */
    public static Stream<Field> flat(final Class<?> subject) {
        return fieldsOf(Classes.optional(subject));
    }

    /**
     * Streams all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    public static Stream<Field> deep(final Class<?> subject) {
        return fieldsOf(Classes.deep(subject));
    }

    /**
     * Streams all {@link Field}s declared by a given {@link Class}, any of its superclasses or any of its
     * superinterfaces.
     */
    public static Stream<Field> wide(final Class<?> subject) {
        return fieldsOf(Classes.wide(subject));
    }

    /**
     * Determines a canonical, fully qualified name for a given field.
     */
    public static String canonicalName(final Field field) {
        return field.getDeclaringClass().getCanonicalName() + "." + field.getName();
    }

    private static Stream<Field> fieldsOf(final Stream<Class<?>> classes) {
        return classes.map(Class::getDeclaredFields)
                .map(Stream::of)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty);
    }

    /**
     * Defines some typical {@link Function}s that serve to find a name for a {@link Field}.
     */
    public interface Naming extends Function<Field, String> {

        /**
         * A {@link Function} that simply returns the plain {@linkplain Field#getName() name} of a given {@link Field}.
         */
        Naming SIMPLE = Field::getName;

        /**
         * A {@link Function} that returns a canonical, full qualified name for a given {@link Field}.
         */
        Naming CANONICAL = Fields::canonicalName;

        /**
         * Defines some typical {@link Function}s that serve to find a name for a {@link Field}
         * that is as unique as possible in the context of a particular class.
         */
        interface ContextSensitive extends Function<Class<?>, Function<Field, String>> {

            /**
             * A {@link Function} that simply returns the plain {@linkplain Field#getName() name} of the given
             * {@link Field} if inquired in the context of the Field's declaring class. Otherwise it returns a
             * canonical, full qualified name.
             */
            ContextSensitive QUALIFIED = context -> field -> context.equals(field.getDeclaringClass())
                    ? field.getName()
                    : canonicalName(field);

            /**
             * A {@link Function} that returns the plane {@linkplain Field#getName() name} of the given {@link Field},
             * preceded by a corresponding number of points (".") depending on the distance of the context to the
             * declaring class of the field.
             */
            ContextSensitive COMPACT = context -> field -> Stream.generate(() -> ".")
                    .limit(Classes.distance(context,
                            field.getDeclaringClass()))
                    .collect(Collectors.joining("", "", field.getName()));
        }
    }

    /**
     * Provides some predefined {@linkplain Predicate filters} for {@link Field Fields}.
     */
    public enum Filter implements Predicate<Field> {

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

        Filter(final IntPredicate filter) {
            this.filter = filter;
        }

        @Override
        public final boolean test(final Field field) {
            return filter.test(field.getModifiers());
        }
    }
}
