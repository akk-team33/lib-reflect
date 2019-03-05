package de.team33.libs.reflect.v3;

import java.util.function.Function;
import java.util.stream.Stream;


class Distance {

    private final Class<?> superClass;
    private final Function<Class<?>, Stream<Class<?>>> superClasses;

    Distance(final Class<?> superClass, final Function<Class<?>, Stream<Class<?>>> superClasses) {
        this.superClass = superClass;
        this.superClasses = superClasses;
    }

    final int from(final Class<?> subClass) {
        return (subClass == superClass) ? 0 : (1 + from(superClasses.apply(subClass)));
    }

    private int from(final Stream<Class<?>> subClasses) {
        return subClasses
            .filter(superClass::isAssignableFrom)
            .map(this::from)
            .reduce(Math::min)
            .orElseThrow(IllegalStateException::new);
    }
}
