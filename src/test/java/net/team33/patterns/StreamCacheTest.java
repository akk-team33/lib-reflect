package net.team33.patterns;

import de.team33.libs.reflect.v3.Fields;
import de.team33.test.reflect.common.Sample;
import de.team33.test.reflect.common.SampleEx;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamCacheTest {

    @Test
    public final void from() {
        final StreamCache<Class<?>, Field> subject = StreamCache.builder(Fields::flat)
                .build();
        Assert.assertEquals(
                Fields.flat(Sample.class).collect(Collectors.toSet()),
                subject.from(Sample.class).collect(Collectors.toSet())
        );
    }

    @Test
    public final void fromWithFilter() {
        final StreamCache<Class<?>, Field> subject = StreamCache.builder(Fields::deep)
                .addFilter(Fields.Filter.INSTANCE)
                .addFilter(Fields.Filter.TRANSIENT.negate())
                .addFilter(Fields.Filter.PUBLIC)
                .build();
        final Set<Field> expected = Fields.deep(SampleEx.class)
                .filter(Fields.Filter.INSTANCE)
                .filter(Fields.Filter.TRANSIENT.negate())
                .filter(Fields.Filter.PUBLIC)
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
