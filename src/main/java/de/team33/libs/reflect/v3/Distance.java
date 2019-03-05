package de.team33.libs.reflect.v3;

import java.util.function.Function;
import java.util.stream.Stream;


public class Distance
{

  private static final Function<Class<?>, Stream<Class<?>>> WIDE = Distance::superClasses;
  private static final Function<Class<?>, Stream<Class<?>>> DEEP = Distance::superClass;

  private final Class<?> superClass;
  private final Function<Class<?>, Stream<Class<?>>> superClasses;

  private Distance(final Class<?> superClass, final Function<Class<?>, Stream<Class<?>>> superClasses)
  {
    this.superClass = superClass;
    this.superClasses = superClasses;
  }

  public static Distance to(final Class<?> superClass)
  {
    return new Distance(superClass, superClass.isInterface() ? WIDE : DEEP);
  }

  public final int from(final Class<?> subClass)
  {
    return superClass == subClass ? 0 : 1 + from(superClasses.apply(subClass));
  }

  private int from(final Stream<Class<?>> subClasses)
  {
    return subClasses
      .filter(superClass::isAssignableFrom)
      .map(this::from)
      .reduce(Math::min)
      .orElseThrow(IllegalStateException::new);
  }

  private static Stream<Class<?>> superClass(final Class<?> subClass) {
    final Class<?> result = subClass.getSuperclass();
    return (null == result) ? Stream.empty() : Stream.of(result);
  }

  private static Stream<Class<?>> superClasses(final Class<?> subClass) {
    return Stream.concat(Stream.of(subClass.getInterfaces()), superClass(subClass));
  }
}
