package net.team33.patterns;

import net.team33.reflect.FieldFilter;
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
        final StreamCache<Class<?>, Field> subject = StreamCache.builder(Fields.FLAT)
                .build();
        Assert.assertEquals(
                Fields.FLAT.apply(Sample.class).collect(Collectors.toSet()),
                subject.from(Sample.class).collect(Collectors.toSet())
        );
    }

    @Test
    public final void fromWithFilter() {
        final StreamCache<Class<?>, Field> subject = StreamCache.builder(Fields.DEEP)
                .addFilter(FieldFilter.INSTANCE)
                .addFilter(FieldFilter.TRANSIENT.negate())
                .addFilter(FieldFilter.PUBLIC)
                .build();
        final Set<Field> expected = Fields.DEEP.apply(SampleEx.class)
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