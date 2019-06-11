package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;


/**
 * A {@link FieldMapping} is primarily a {@link Function} that can map a given {@link Class} to a {@link Map},
 * which in turn maps {@link String field names} to {@link Field}s of the given class.
 *
 * <p>A {@link FieldMapping} is primarily a {@link Function} that can generate {@link Map}s from given
 * {@link Class}es, which in turn maps {@link String field names} (as key) to {@link Field}s of the
 * {@link Class} (as value).</p>
 *
 * <p>A {@link FieldMapping} is primarily a {@link Function} that maps a given {@link Class} to a {@link Map}.
 * The {@link Map} associates {@link Field}s of the given {@link Class} (as values) with their logical
 * field names (as keys).</p>
 */
public interface FieldMapping extends Function<Class<?>, Map<String, Field>>
{

}
