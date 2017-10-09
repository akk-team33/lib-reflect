package net.team33.reflect;

import net.team33.reflect.test.Sample;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class FieldEntryTest {

    private final Sample sample = new Sample();

    private static Map<String, Field> fieldMap() {
        return FieldStream.FLAT.apply(Sample.class)
                .peek(field -> field.setAccessible(true))
                .collect(TreeMap::new, (map, field) -> map.put(field.getName(), field), Map::putAll);
    }

    @Test
    public final void getKey() {
        fieldMap().entrySet().forEach(entry -> {
            final Map.Entry<String, Object> subject = new FieldEntry(entry, sample);
            Assert.assertEquals(entry.getKey(), subject.getKey());
        });
    }

    @Test
    public final void getValue() {
        fieldMap().entrySet().forEach(entry -> {
            final Map.Entry<String, Object> subject = new FieldEntry(entry, sample);
            Assert.assertEquals("Sample." + entry.getKey(), subject.getValue());
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void setValue() {
        fieldMap().entrySet().forEach(entry -> {
            new FieldEntry(entry, sample).setValue(new Object());
        });
    }
}