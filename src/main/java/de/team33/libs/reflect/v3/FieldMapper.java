package de.team33.libs.reflect.v3;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class FieldMapper
{

  private final Function<Class<?>, Stream<Field>> toFields = Fields::deep;

  private final Function<Class<?>, Function<Field, String>> contextToName = c -> Field::getName;

  private final Function<Class<?>, Predicate<? super Field>> contextToFilter = c -> f -> true;

  private final Consumer<Field> modifier = field -> field.setAccessible(true);

  public final Map<String, Field> map(final Class<?> subject)
  {
    final Function<Field, String> toName = contextToName.apply(subject);
    final Predicate<? super Field> filter = contextToFilter.apply(subject);
    return unmodifiableMap(toFields.apply(subject)
                                   .filter(filter)
                                   .peek(modifier)
                                   .collect(toMap(toName, field -> field, (a, b) -> b, TreeMap::new)));
  }
}
