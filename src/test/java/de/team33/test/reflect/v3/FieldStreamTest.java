package de.team33.test.reflect.v3;

import de.team33.libs.reflect.v3.FieldStream;
import net.team33.reflect.test.Sample;
import net.team33.reflect.test.SampleEx;
import org.junit.Assert;
import org.junit.Test;

public class FieldStreamTest {

    @Test
    public final void flat() throws Exception {
        Assert.assertEquals(12, FieldStream.FLAT.apply(Sample.class).count());
        Assert.assertEquals(12, FieldStream.FLAT.apply(SampleEx.class).count());
        Assert.assertEquals(0, FieldStream.FLAT.apply(SampleXX.class).count());
    }

    @Test
    public final void deep() throws Exception {
        Assert.assertEquals(12, FieldStream.DEEP.apply(Sample.class).count());
        Assert.assertEquals(24, FieldStream.DEEP.apply(SampleEx.class).count());
        Assert.assertEquals(24, FieldStream.DEEP.apply(SampleXX.class).count());
    }

    private static class SampleXX extends SampleEx {
    }
}