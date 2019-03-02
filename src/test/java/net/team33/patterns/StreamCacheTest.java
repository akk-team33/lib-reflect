package net.team33.patterns;

import net.team33.reflect.FieldFilter;
import de.team33.libs.reflect.v3.FieldStream;
import net.team33.reflect.test.Sample;
import net.team33.reflect.test.SampleEx;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamCacheTest {

    @Test
    public final void from() {
        final StreamCache<Class<?>, Field> subject = StreamCache.builder(FieldStream.FLAT)
                .build();
        Assert.assertEquals(
                FieldStream.FLAT.apply(Sample.class).collect(Collectors.toSet()),
                subject.from(Sample.class).collect(Collectors.toSet())
        );
    }

    @Test
    public final void fromWithFilter() {
        final StreamCache<Class<?>, Field> subject = StreamCache.builder(FieldStream.DEEP)
                .addFilter(FieldFilter.INSTANCE)
                .addFilter(FieldFilter.TRANSIENT.negate())
                .addFilter(FieldFilter.PUBLIC)
                .build();
        final Set<Field> expected = FieldStream.DEEP.apply(SampleEx.class)
                .filter(FieldFilter.INSTANCE)
                .filter(FieldFilter.TRANSIENT.negate())
                .filter(FieldFilter.PUBLIC)
                .collect(Collectors.toSet());
        final Set<Field> result = subject.from(SampleEx.class)
                .collect(Collectors.toSet());
        Assert.assertEquals(
                expected,
                result
        );
        Assert.assertEquals(
                result,
                subject.from(SampleEx.class)
                        .collect(Collectors.toSet())
        );
    }
}