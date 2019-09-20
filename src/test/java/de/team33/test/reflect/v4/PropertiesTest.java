package de.team33.test.reflect.v4;

import de.team33.libs.reflect.v4.Properties;
import de.team33.libs.reflect.v4.Property;
import de.team33.test.reflect.common.Level3;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

public class PropertiesTest {

    @Test
    public void of() {
        final Set<String> expected = new TreeSet<>(Arrays.asList(
                "privateFinalInt",
                "privateFinalString",
                "privateFinalDate"
        ));
        final TreeSet<String> result = Properties.of(Level3.class).stream()
                .map(Property::getName)
                .collect(toCollection(TreeSet::new));
        Assert.assertEquals(expected, result);
    }
}
