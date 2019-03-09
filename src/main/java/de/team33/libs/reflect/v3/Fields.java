package de.team33.libs.reflect.v3;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    return fieldsOf(Classes.optional(subject));
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

  /**
   * TODO
   */
  public static String fullQualifiedName(final Field field)
  {
    return field.getDeclaringClass().getCanonicalName() + "." + field.getName();
  }

  private static Stream<Field> fieldsOf(final Stream<Class<?>> classes)
  {
    return classes.map(Class::getDeclaredFields)
                  .map(Stream::of)
                  .reduce(Stream::concat)
                  .orElseGet(Stream::empty);
  }

  public interface Naming extends Function<Field, String>
  {

    Naming SIMPLE = Field::getName;

    Naming FULL_QUALIFIED = Fields::fullQualifiedName;


    interface Conditional extends Function<Class<?>, Function<Field, String>>
    {

      Conditional QUALIFIED = context -> field -> {
        if (context.equals(field.getDeclaringClass()))
          return field.getName();
        else
          return fullQualifiedName(field);
      };

      Conditional COMPACT = context -> field -> Stream.generate(() -> ".")
                                                      .limit(Classes.distance(context,
                                                                              field.getDeclaringClass()))
                                                      .collect(Collectors.joining("", "", field.getName()));
    }
  }
}
