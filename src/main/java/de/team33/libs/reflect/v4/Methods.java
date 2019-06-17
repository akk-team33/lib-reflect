package de.team33.libs.reflect.v4;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class Methods {

  public static final String GETTER_PREFIX = "get";
  public static final Set<String> GETTER_BLACKLIST = Collections.singleton("getClass");

  /**
   * Streams all public {@link Method}s provided by a given {@link Class}.
   */
  public static Stream<Method> publicStreamOf(final Class<?> type) {
    return Stream.of(type.getMethods());
  }

  @FunctionalInterface
  public interface Filter extends Predicate<Method> {

    Filter GETTERS = method -> {
      final String name = method.getName();
      return (0 == method.getParameterCount())
             && !Void.TYPE.equals(method.getReturnType())
             && name.startsWith(GETTER_PREFIX)
             && !GETTER_BLACKLIST.contains(name);
    };
  }

  @FunctionalInterface
  public interface Streaming extends Function<Class<?>, Stream<Method>> {

    Streaming PUBLIC = Methods::publicStreamOf;

    Streaming PUBLIC_GETTERS = type ->
      publicStreamOf(type).filter(Filter.GETTERS);
  }
}
