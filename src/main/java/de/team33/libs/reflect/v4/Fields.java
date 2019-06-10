package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;


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
     * <p>Returns a {@link Mapping.Builder Builder} for {@link Mapping Mapping}s.</p>
     * <p>A mapping is a {@link Function} that returns a {@link Map Map&lt;String, Field&gt;}
     * from a given {@link Class}</p>
     * <p>The key values ({@link String}) of such a {@link Map} are the logical names of the associated
     * {@link Field}s.</p>
     */
    public static Mapping.Builder mapping() {
        return new Mapping.Builder();
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
     * Defines some typical {@link Function}s that serve to stream {@link Field}s of a {@link Class}.
     */
    public interface Streaming extends Function<Class<?>, Stream<Field>> {

        /**
         * Streams all {@link Field}s straightly declared by a given {@link Class}
         */
        Streaming FLAT = Fields::flat;

        /**
         * Streams all {@link Field}s declared by a given {@link Class} or any of its superclasses.
         */
        Streaming DEEP = Fields::deep;

        /**
         * Streams all {@link Field}s declared by a given {@link Class}, any of its superclasses or any of
         * its superinterfaces.
         */
        Streaming WIDE = Fields::wide;

        /**
         * Streams all non-static {@link Field}s declared by a given {@link Class} or any of its
         * superclasses.
         */
        Streaming INSTANCE = context -> deep(context).filter(Filter.INSTANCE);

        /**
         * Streams all non-static/non-transient {@link Field}s straightly declared by a given {@link Class}.
         * Those fields should be significant for a simple type with value semantics.
         */
        Streaming SIGNIFICANT_FLAT = context -> flat(context).filter(Filter.SIGNIFICANT);

        /**
         * Streams all non-static/non-transient {@link Field}s declared by a given {@link Class} or any of
         * its superclasses. Those fields should be significant for a type with value semantics.
         */
        Streaming SIGNIFICANT_DEEP = context -> deep(context).filter(Filter.SIGNIFICANT);
    }

    /**
     * <p>A Mapping is a {@link Function} that returns a {@link Map Map&lt;String, Field&gt;}
     * from a given {@link Class}</p>
     * <p>The key values ({@link String}) of such a {@link Map} are the logical names of the associated
     * {@link Field}s.</p>
     */
    @FunctionalInterface
    public interface Mapping extends Function<Class<?>, Map<String, Field>> {

        /**
         * Defines a {@link Mapping} that only considers the fields straightly declared by the underlying class,
         * which are neither static nor transient.
         */
        Mapping SIGNIFICANT_FLAT = mapping()
                .setToFieldStream(Streaming.SIGNIFICANT_FLAT)
                .setToName(Naming.SIMPLE)
                .build();

        /**
         * Defines a {@link Mapping} that considers the fields declared by the underlying class or one of its
         * superclasses, which are neither static nor transient.
         */
        Mapping SIGNIFICANT_DEEP = mapping().build();

        /**
         * A for {@link Mapping} instances.
         */
        class Builder {

            private Function<Class<?>, Stream<Field>> toFieldStream = Streaming.SIGNIFICANT_DEEP;
            private Function<Class<?>, Function<Field, String>> toNaming = Naming.ContextSensitive.COMPACT;

            private Builder() {
            }

            /**
             * <p>Specifies how to get a {@link Stream} of {@link Field}s from a given {@link Class}.</p>
             * <p>Default is {@link Streaming#SIGNIFICANT_DEEP}.</p>
             *
             */
            public final Builder setToFieldStream(final Function<Class<?>, Stream<Field>> toFieldStream) {
                this.toFieldStream = toFieldStream;
                return this;
            }

            /**
             * <p>Specifies how a name results from a given {@link Field} in the context of a given {@link Class}.</p>
             * <p>Default is {@link Naming.ContextSensitive#COMPACT}.</p>
             */
            public final Builder setToNaming(final Function<Class<?>, Function<Field, String>> toNaming) {
                this.toNaming = toNaming;
                return this;
            }

            /**
             * Specifies how a name results from a given {@link Field}.
             */
            public final Builder setToName(final Function<Field, String> toName) {
                return setToNaming(ignored -> toName);
            }

            /**
             * Retrieves a new {@link Mapping} that uses the specified methods.
             */
            public final Mapping build() {
                return new Mapper(this);
            }
        }
    }

    /**
     * A tool to create a {@link Map} to {@link Fields} from their logical names.
     */
    private static class Mapper implements Mapping {

        private Function<Class<?>, Stream<Field>> toFieldStream;
        private Function<Class<?>, Function<Field, String>> toNaming;

        private Mapper(final Mapping.Builder builder) {
            toFieldStream = builder.toFieldStream;
            toNaming = builder.toNaming;
        }

        /**
         * Retrieves a {@link Map} to {@link Fields} from their logical names.
         */
        @Override
        public final Map<String, Field> apply(final Class<?> subject) {
            final Function<Field, String> toName = toNaming.apply(subject);
            return unmodifiableMap(toFieldStream.apply(subject)
                    .peek(field -> field.setAccessible(true))
                    .collect(toMap(toName, field -> field, (a, b) -> b, TreeMap::new)));
        }
    }
}
