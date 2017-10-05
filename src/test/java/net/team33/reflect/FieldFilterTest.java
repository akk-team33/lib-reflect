package net.team33.reflect;

import net.team33.reflect.test.Sample;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("JUnit4MethodNamingConvention")
public class FieldFilterTest {

    private static Set<String> sampleFields() {
        return sampleFields(FieldStream.FLAT.apply(Sample.class));
    }

    private static Set<String> sampleFields(final Predicate<Field> filter) {
        return sampleFields(FieldStream.FLAT.apply(Sample.class)
                .filter(filter));
    }

    private static Set<String> sampleFields(final Stream<Field> stream) {
        return stream
                .map(FieldName.SIMPLE)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    @Test
    public final void ANY() {
        final Set<String> expected = sampleFields();
        final Set<String> result = sampleFields(FieldFilter.ANY);
        assertEquals(expected, result);
    }

    @Test
    public final void PUBLIC() {
        sampleFields(FieldFilter.PUBLIC)
                .forEach(name -> assertTrue(name, name.contains("Public")));
    }

    @Test
    public final void PRIVATE() {
        sampleFields(FieldFilter.PRIVATE)
                .forEach(name -> assertTrue(name, name.contains("Private")));
    }

    @Test
    public final void PROTECTED() {
        sampleFields(FieldFilter.PROTECTED)
                .forEach(name -> assertTrue(name, name.contains("Protected")));
    }

    @Test
    public final void STATIC() {
        sampleFields(FieldFilter.STATIC)
                .forEach(name -> assertTrue(name, name.contains("Static")));
    }

    @Test
    public final void FINAL() {
        sampleFields(FieldFilter.FINAL)
                .forEach(name -> assertTrue(name, name.contains("Final")));
    }

    @Test
    public final void TRANSIENT() {
        sampleFields(FieldFilter.TRANSIENT)
                .forEach(name -> assertTrue(name, name.contains("Transient")));
    }

    @Test
    public final void INSTANCE() {
        sampleFields(FieldFilter.INSTANCE)
                .forEach(name -> assertFalse(name, name.contains("Static")));
    }

    @Test
    public final void SIGNIFICANT() {
        sampleFields(FieldFilter.SIGNIFICANT)
                .forEach(name -> {
                    assertFalse(name, name.contains("Static"));
                    assertFalse(name, name.contains("Transient"));
                });
    }
}
