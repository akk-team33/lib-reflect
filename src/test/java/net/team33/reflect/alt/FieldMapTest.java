package net.team33.reflect.alt;

import net.team33.reflect.test.Randomizer;
import net.team33.reflect.test.Sample;
import org.junit.Assert;
import org.junit.Test;

public class FieldMapTest {

    private final FieldMapper<Sample> mapper = FieldMapper.of(Sample.class).build();
    private Randomizer randomizer = new Randomizer();

    @Test
    public final void sample() {
        final Sample sample = new Sample(randomizer);
        final Sample result = mapper.map(mapper.map(sample)).to(new Sample());
        Assert.assertEquals(sample, result);
    }
}