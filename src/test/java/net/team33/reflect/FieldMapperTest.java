package net.team33.reflect;

import de.team33.libs.reflect.v3.Fields;
import net.team33.reflect.test.Randomizer;
import net.team33.reflect.test.Sample;
import net.team33.reflect.test.SampleEx;
import org.junit.Assert;
import org.junit.Test;

public class FieldMapperTest {

    private final Randomizer randomizer = new Randomizer();

    @Test
    public final void sample() {
        final FieldMapper<Sample> mapper = FieldMapper.of(Sample.class).build();
        final Sample origin = new Sample(randomizer);
        final Sample target = new Sample();
        final Sample result = mapper.map(mapper.map(origin)).to(target);
        Assert.assertSame(target, result);
        Assert.assertNotSame(origin, result);
        Assert.assertEquals(origin, result);
    }

    @Test
    public final void sampleEx() {
        final FieldMapper<SampleEx> mapperEx = FieldMapper.of(SampleEx.class)
                .setToFieldStream(Fields.DEEP)
                .setToFieldNameByClass(FieldName.PREFIXED)
                .build();
        final SampleEx origin = new SampleEx(randomizer);
        final SampleEx target = new SampleEx();
        final SampleEx result = mapperEx.map(mapperEx.map(origin)).to(target);
        Assert.assertSame(target, result);
        Assert.assertNotSame(origin, result);
        Assert.assertEquals(origin, result);
    }
}