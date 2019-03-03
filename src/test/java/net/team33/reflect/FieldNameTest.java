package net.team33.reflect;

import de.team33.libs.reflect.v3.Fields;
import de.team33.test.reflect.common.Sample;
import de.team33.test.reflect.common.SampleEx;
import org.junit.Assert;
import org.junit.Test;

public class FieldNameTest {

    @Test
    public final void simple() {
        Fields.FLAT.apply(Sample.class).forEach(field -> Assert.assertEquals(
                field.getName(),
                FieldName.SIMPLE.apply(field)
        ));
    }

    @Test
    public final void qualified() {
        Fields.FLAT.apply(Sample.class).forEach(field -> {
            Assert.assertEquals(
                    "de.team33.test.reflect.common.Sample." + field.getName(),
                    FieldName.QUALIFIED.apply(field)
            );
            Assert.assertEquals(
                    "de.team33.test.reflect.common.Sample." + field.getName(),
                    FieldName.QUALIFIED.apply(field)
            );
            Assert.assertEquals(
                    "de.team33.test.reflect.common.Sample." + field.getName(),
                    FieldName.QUALIFIED.apply(field)
            );
        });
    }

    @Test
    public final void prefixed() {
        Fields.FLAT.apply(Sample.class).forEach(field -> {
            Assert.assertEquals(
                    field.getName(),
                    FieldName.PREFIXED.apply(Sample.class).apply(field)
            );
            Assert.assertEquals(
                    "." + field.getName(),
                    FieldName.PREFIXED.apply(SampleEx.class).apply(field)
            );
            Assert.assertEquals(
                    ".." + field.getName(),
                    FieldName.PREFIXED.apply(SampleXX.class).apply(field)
            );
        });
    }

    @SuppressWarnings({"ClassTooDeepInInheritanceTree", "EmptyClass"})
    private static class SampleXX extends SampleEx {
    }
}