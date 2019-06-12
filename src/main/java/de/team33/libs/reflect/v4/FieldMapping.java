package de.team33.libs.reflect.v4;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * In essence, a {@link FieldMapping} is a {@link Function} to get a {@link Map} whose values represent
 * a subset of all {@link Field}s declared by a given {@link Class} or one of its superclasses.
 * The keys are to be understood as logical field names, but they do not necessarily have to match the
 * {@linkplain Field#getName() actual field names}.
 */
public class FieldMapping implements Function<Class<?>, Map<String, Field>> {

    /**
     * Defines a {@link FieldMapping} that only considers the fields straightly declared by the given
     * {@link Class}, which are neither static nor transient.
     */
    public static final FieldMapping SIGNIFICANT_FLAT = new FieldMapping(
        Fields.Streaming.SIGNIFICANT_FLAT,
        ignored -> Fields.Naming.SIMPLE
    );

    /**
     * Defines a {@link FieldMapping} that only considers the fields straightly declared by the given
     * {@link Class}, which are neither static nor transient.
     */
    public static final FieldMapping SIGNIFICANT_DEEP = new FieldMapping(
        Fields.Streaming.SIGNIFICANT_DEEP,
        Fields.Naming.Hierarchical.COMPACT
    );

    private final Function<Class<?>, Stream<Field>> toFieldStream;
    private final Function<Class<?>, Function<Field, String>> toNaming;

    /**
     * Initializes a new instance.
     */
    public FieldMapping(final Function<Class<?>, Stream<Field>> toFieldStream,
                        final Function<Class<?>, Function<Field, String>> toNaming) {
        this.toFieldStream = toFieldStream;
        this.toNaming = toNaming;
    }

    @Override
    public Map<String, Field> apply(final Class<?> aClass) {
        final Function<Field, String> toName = toNaming.apply(aClass);
        return toFieldStream.apply(aClass)
                            .peek(field -> field.setAccessible(true))
                            .collect(toMap(toName, field -> field));
    }
}
