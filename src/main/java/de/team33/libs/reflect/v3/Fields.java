package de.team33.libs.reflect.v3;

import java.lang.reflect.Field;
import java.util.stream.Stream;


/**
 * Utility for dealing with fields.
 */
public class Fields
{

  /**
   * Streams all {@link Field}s straightly declared by a given {@link Class}
   */
  public static Stream<Field> flat(final Class<?> subject)
  {
    return fieldsOf(Classes.flat(subject));
  }

  /**
   * Streams all {@link Field}s declared by a given {@link Class} or any of its superclasses.
   */
  public static Stream<Field> deep(final Class<?> subject)
  {
    return fieldsOf(Classes.deep(subject));
  }

  /**
   * Streams all {@link Field}s declared by a given {@link Class}, any of its superclasses or any of its
   * superinterfaces.
   */
  public static Stream<Field> wide(final Class<?> subject)
  {
    return fieldsOf(Classes.wide(subject));
  }

  private static Stream<Field> fieldsOf(final Stream<Class<?>> classes)
  {
    return classes.map(Class::getDeclaredFields)
                  .map(Stream::of)
                  .reduce(Stream::concat)
                  .orElseGet(Stream::empty);
  }
}
