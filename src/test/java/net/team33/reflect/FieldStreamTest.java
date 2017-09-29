package net.team33.reflect;

import net.team33.reflect.reflect.fields.ToStream;
import net.team33.reflect.test.Sample;
import net.team33.reflect.test.SampleEx;
import org.junit.Assert;
import org.junit.Test;

public class FieldStreamTest {

    @Test
    public final void flat() throws Exception {
        Assert.assertEquals(12, ToStream.FLAT.apply(Sample.class).count());
        Assert.assertEquals(12, ToStream.FLAT.apply(SampleEx.class).count());
        Assert.assertEquals(0, ToStream.FLAT.apply(SampleXX.class).count());
    }

    @Test
    public final void deep() throws Exception {
        Assert.assertEquals(12, ToStream.DEEP.apply(Sample.class).count());
        Assert.assertEquals(24, ToStream.DEEP.apply(SampleEx.class).count());
        Assert.assertEquals(24, ToStream.DEEP.apply(SampleXX.class).count());
    }

    private static class SampleXX extends SampleEx {
    }
}