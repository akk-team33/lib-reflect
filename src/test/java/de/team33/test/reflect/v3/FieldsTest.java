package de.team33.test.reflect.v3;

import de.team33.libs.reflect.v3.Fields;
import de.team33.test.reflect.common.Sample;
import de.team33.test.reflect.common.SampleEx;
import org.junit.Assert;
import org.junit.Test;

public class FieldsTest {

    @Test
    public final void flat() throws Exception {
        Assert.assertEquals(12, Fields.FLAT.apply(Sample.class).count());
        Assert.assertEquals(12, Fields.FLAT.apply(SampleEx.class).count());
        Assert.assertEquals(0, Fields.FLAT.apply(SampleXX.class).count());
    }

    @Test
    public final void deep() throws Exception {
        Assert.assertEquals(12, Fields.DEEP.apply(Sample.class).count());
        Assert.assertEquals(24, Fields.DEEP.apply(SampleEx.class).count());
        Assert.assertEquals(24, Fields.DEEP.apply(SampleXX.class).count());
    }

    private static class SampleXX extends SampleEx {
    }
}