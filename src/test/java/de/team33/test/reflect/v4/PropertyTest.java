package de.team33.test.reflect.v4;

import de.team33.libs.reflect.v4.Property;
import de.team33.test.reflect.common.AnyBean;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.stream.Stream;

public class PropertyTest {

    @Test
    public void get() {
        final Property<AnyBean> privateInt = Property.simple("PrivateInt", AnyBean::getPrivateInt, AnyBean::setPrivateInt);
        final AnyBean anyBean = new AnyBean();

        intStream().forEach(value -> {
            anyBean.setPrivateInt(value);
            final Object result = privateInt.get(anyBean);
            Assert.assertEquals(value, result);
        });
    }

    @Test
    public void set() {
        final Property<AnyBean> privateInt = Property.simple("PrivateInt", AnyBean::getPrivateInt, AnyBean::setPrivateInt);
        final AnyBean anyBean = new AnyBean();

        intStream().forEach(value -> {
            privateInt.set(anyBean, value);
            Assert.assertEquals(value.intValue(), anyBean.getPrivateInt());
        });
    }

    private Stream<Integer> intStream() {
        return Stream.concat(Stream.of(0, 1, 2, 7, 13, 17, 29), Stream.generate(() -> new Random().nextInt()))
                     .distinct()
                     .limit(100);
    }
}