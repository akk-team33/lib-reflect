package de.team33.test.reflect.v4;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.reflect.v4.Properties;
import de.team33.libs.reflect.v4.Property;
import de.team33.test.reflect.common.AnyBean;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

public class PropertiesTest {

    private static final Properties<AnyBean> BI_PROPERTIES = Properties.builder(AnyBean.class)
            .add(Property.setup("privateInt", AnyBean::getPrivateInt, AnyBean::setPrivateInt))
            .add(Property.setup("privateString", AnyBean::getPrivateString, AnyBean::setPrivateString))
            .add(Property.setup("privateDate", AnyBean::getPrivateDate, AnyBean::setPrivateDate))
            .build();

    @Test
    public final void map() {

        final AnyBean original = new AnyBean()
                .setPrivateInt(278)
                .setPrivateString("a string")
                .setPrivateDate(new Date());
        final Map<String, Object> stage = BI_PROPERTIES.map(original);
        final AnyBean result = BI_PROPERTIES.remap(stage, new AnyBean());
        Assert.assertEquals(original, result);
    }

    @Test
    public final void remap() {
        final Map<String, Object> original = ImmutableMap.<String, Object>builder()
                .put("privateInt", 278)
                .put("privateString", "a string")
                .put("privateDate", new Date())
                .build();
        final AnyBean stage = BI_PROPERTIES.remap(original, new AnyBean());
        final Map<String, Object> result = BI_PROPERTIES.map(stage);
        Assert.assertEquals(original, result);
    }
}