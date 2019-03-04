package de.team33.libs.reflect.v3;

import java.util.stream.Stream;


/**
 * Utility for dealing with classes.
 */
public class Classes {

    private static final int MAX_DISTANCE = Short.MAX_VALUE;

    public static int distance(final Class<?> subClass, final Class<?> superClass) {
        if (subClass == superClass) {
            return 0;
        } else if (Object.class == superClass) {
            return 1 + distance(subClass.getSuperclass(), superClass);
        } else {
            return 1 + distance(superClasses(subClass), superClass);
        }
    }

    private static Stream<Class<?>> superClasses(final Class<?> subClass) {
        return Stream.concat(Stream.of(subClass.getInterfaces()), flat(subClass.getSuperclass()));
    }

    private static int distance(final Stream<Class<?>> subClasses, final Class<?> superClass) {
        return subClasses
                .filter(superClass::isAssignableFrom)
                .map(subClass -> distance(subClass, superClass))
                .reduce(Math::min)
                .orElseThrow(() -> new IllegalStateException());
    }

    /**
     * Streams a 'flat' class hierarchy (that focuses only on the specified class).
     */
    public static Stream<Class<?>> flat(final Class<?> subject) {
        return (null == subject) ? Stream.empty() : Stream.of(subject);
    }

    /**
     * Streams a class hierarchy that focuses on the specified class and its superclasses.
     */
    public static Stream<Class<?>> deep(final Class<?> subject) {
        return (null == subject) ? Stream.empty() :
                Stream.concat(deep(subject.getSuperclass()), Stream.of(subject));
    }

    /**
     * Streams a class hierarchy that focuses on the specified class, its superclasses and its superinterfaces.
     */
    public static Stream<Class<?>> wide(final Class<?> subject) {
        return broad(subject).distinct();
    }

    private static Stream<Class<?>> broad(final Class<?> subject) {
        return (null == subject) ? Stream.empty() :
                broad(subject.getInterfaces(), subject.getSuperclass(), subject);
    }

    private static Stream<Class<?>> broad(final Class<?>[] interfaces,
                                          final Class<?> superclass,
                                          final Class<?> subject) {
        return Stream.concat(broad(interfaces), Stream.concat(broad(superclass), Stream.of(subject)));
    }

    private static Stream<Class<?>> broad(final Class<?>[] subjects) {
        return Stream.of(subjects).map(Classes::broad).reduce(Stream::concat).orElseGet(Stream::empty);
    }
}
