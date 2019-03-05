package de.team33.libs.reflect.v3;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FieldMap extends AbstractMap<String, Field>
{

  private final Map<String, Field> backing;

  private FieldMap(final Class<?> subject, final Template template)
  {
    final Function<Field, String> toName = template.toName.apply(subject);
    backing = unmodifiableMap(template.toFields.apply(subject)
                                               .filter(template.filter)
                                               .collect(toMap(toName,
                                                              field -> field,
                                                              (a, b) -> b,
                                                              TreeMap::new)));
  }

  public static Template template()
  {
    throw new UnsupportedOperationException("not yet implemented");
  }

  @Override
  public final Set<Entry<String, Field>> entrySet()
  {
    return backing.entrySet();
  }

  public static class Template
  {

    private final Function<Class<?>, Stream<Field>> toFields = Fields::deep;
    private final Function<Class<?>, Function<Field, String>> toName = subject -> Field::getName;
    private final Predicate<? super Field> filter = field -> true;

    public final FieldMap apply(final Class<?> subject)
    {
      return new FieldMap(subject, this);
    }
  }
}
