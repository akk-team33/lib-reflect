package de.team33.libs.reflect.v4;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Properties<T> {

    private final List<Property> backing;

    private Properties(final Stream<Property> properties) {
        this.backing = properties.collect(Collectors.toCollection(LinkedList::new));
    }

    public static <T> Properties<T> of(final Class<T> type) {
        return new Properties<>(Strategy.FIELD_WISE.apply(type));
    }

    private static Stream<Property> streamOfFields(final Class<?> type) {
        return Fields.Streaming.SIGNIFICANT_DEEP.apply(type).map(field -> new FieldProperty(type, field));
    }

    public final Stream<Property> stream() {
        return backing.stream();
    }

    public interface Strategy extends Function<Class<?>, Stream<Property>> {

        Strategy FIELD_WISE = Properties::streamOfFields;
    }
}
