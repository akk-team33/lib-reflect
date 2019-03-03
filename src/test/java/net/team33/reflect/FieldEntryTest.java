package net.team33.reflect;

import de.team33.libs.reflect.v3.Fields;
import net.team33.reflect.test.Sample;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class FieldEntryTest {

    private final Sample sample = new Sample();

    private static Map<String, Field> fieldMap() {
        return Fields.FLAT.apply(Sample.class)
                .peek(field -> field.setAccessible(true))
                .collect(TreeMap::new, (map, field) -> map.put(field.getName(), field), Map::putAll);
    }

    private static Object get(final Field field, final Object subject) {
        try {
            return field.get(subject);
        } catch (final IllegalAccessException caught) {
            throw new IllegalArgumentException(String.format(
                    "not applicable: field <%s> on subject <%s>", field, subject), caught);
        }
    }

    private static Map.Entry<String, Object> newSimpleEntry(final Map.Entry<String, Object> result) {
        return new AbstractMap.SimpleImmutableEntry<>(result.getKey(), result.getValue());
    }

    @Test
    public final void getKey() {

        fieldMap().entrySet().forEach(origin -> {
            final Map.Entry<String, Object> result = new FieldEntry(origin, sample);
            Assert.assertEquals(origin.getKey(), result.getKey());
        });
    }

    @Test
    public final void getValue() {
        fieldMap().entrySet().forEach(origin -> {
            final Map.Entry<String, Object> result = new FieldEntry(origin, sample);
            Assert.assertEquals(get(origin.getValue(), sample), result.getValue());
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void setValue() {
        final Map.Entry<String, Field> origin = fieldMap().entrySet().iterator().next();
        new FieldEntry(origin, sample).setValue("any value");
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public final void testEquals() {
        fieldMap().entrySet().forEach(origin -> {
            final Map.Entry<String, Object> result = new FieldEntry(origin, sample);
            final Map.Entry<String, Object> expected = newSimpleEntry(result);
            Assert.assertTrue(result.equals(expected));
            Assert.assertTrue(expected.equals(result));
        });
    }

    @Test
    public final void testHashCode() {
        fieldMap().entrySet().forEach(origin -> {
            final Map.Entry<String, Object> result = new FieldEntry(origin, sample);
            final Map.Entry<String, Object> expected = newSimpleEntry(result);
            Assert.assertEquals(expected.hashCode(), result.hashCode());
        });
    }

    @Test
    public final void testToString() {
        fieldMap().entrySet().forEach(entry -> {
            final Map.Entry<String, Object> subject = new FieldEntry(entry, sample);
            final Map.Entry<String, Object> expected = newSimpleEntry(subject);
            Assert.assertEquals(expected.toString(), subject.toString());
        });
    }
}