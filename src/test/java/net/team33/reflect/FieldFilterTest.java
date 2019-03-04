package net.team33.reflect;

import de.team33.libs.reflect.v3.Fields;
import de.team33.test.reflect.common.Sample;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("JUnit4MethodNamingConvention")
public class FieldFilterTest {

    private static Stream<Field> sampleFields() {
        return Fields.flat(Sample.class);
    }

    private static Stream<Field> sampleFields(final Predicate<? super Field> filter) {
        return Fields.flat(Sample.class).filter(filter);
    }

    private static Set<String> sampleFieldNames(final Stream<Field> stream) {
        return stream
                .map(FieldName.SIMPLE)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    @Test
    public final void ANY() {
        final Set<String> expected = sampleFieldNames(sampleFields());
        final Set<String> result = sampleFieldNames(sampleFields(FieldFilter.ANY));
        assertEquals(expected, result);
    }

    @Test
    public final void PUBLIC() {
        sampleFields(FieldFilter.PUBLIC)
                .forEach(field -> assertTrue(Modifier.isPublic(field.getModifiers())));
        sampleFields(FieldFilter.PUBLIC.negate())
                .forEach(field -> assertFalse(Modifier.isPublic(field.getModifiers())));
    }

    @Test
    public final void PRIVATE() {
        sampleFields(FieldFilter.PRIVATE)
                .forEach(field -> assertTrue(Modifier.isPrivate(field.getModifiers())));
        sampleFields(FieldFilter.PRIVATE.negate())
                .forEach(field -> assertFalse(Modifier.isPrivate(field.getModifiers())));
    }

    @Test
    public final void PROTECTED() {
        sampleFields(FieldFilter.PROTECTED)
                .forEach(field -> assertTrue(Modifier.isProtected(field.getModifiers())));
        sampleFields(FieldFilter.PROTECTED.negate())
                .forEach(field -> assertFalse(Modifier.isProtected(field.getModifiers())));
    }

    @Test
    public final void STATIC() {
        sampleFields(FieldFilter.STATIC)
                .forEach(field -> assertTrue(Modifier.isStatic(field.getModifiers())));
        sampleFields(FieldFilter.STATIC.negate())
                .forEach(field -> assertFalse(Modifier.isStatic(field.getModifiers())));
    }

    @Test
    public final void FINAL() {
        sampleFields(FieldFilter.FINAL)
                .forEach(field -> assertTrue(Modifier.isFinal(field.getModifiers())));
        sampleFields(FieldFilter.FINAL.negate())
                .forEach(field -> assertFalse(Modifier.isFinal(field.getModifiers())));
    }

    @Test
    public final void TRANSIENT() {
        sampleFields(FieldFilter.TRANSIENT)
                .forEach(field -> assertTrue(Modifier.isTransient(field.getModifiers())));
        sampleFields(FieldFilter.TRANSIENT.negate())
                .forEach(field -> assertFalse(Modifier.isTransient(field.getModifiers())));
    }

    @Test
    public final void INSTANCE() {
        sampleFields(FieldFilter.INSTANCE)
                .forEach(field -> assertFalse(Modifier.isStatic(field.getModifiers())));
        sampleFields(FieldFilter.INSTANCE.negate())
                .forEach(field -> assertTrue(Modifier.isStatic(field.getModifiers())));
    }

    @Test
    public final void SIGNIFICANT() {
        sampleFields(FieldFilter.SIGNIFICANT)
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    assertFalse(Modifier.isStatic(modifiers));
                    assertFalse(Modifier.isTransient(modifiers));
                });
        sampleFields(FieldFilter.SIGNIFICANT.negate())
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    assertTrue(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
                });
    }
}
