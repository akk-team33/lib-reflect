package de.team33.test.reflect.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import de.team33.libs.reflect.v3.Distance;
import org.junit.Test;


public class DistanceTest
{

    @Test
    public void distance() {
        assertEquals(0, Distance.to(Inner.class).from(Inner.class));
        assertEquals(1, Distance.to(Super.class).from(Inner.class));
        assertEquals(2, Distance.to(Base.class).from(Inner.class));
        assertEquals(3, Distance.to(Object.class).from(Inner.class));
        assertEquals(2, Distance.to(ISuper1.class).from(Inner.class));
        assertEquals(1, Distance.to(ISuper2.class).from(Inner.class));
        assertEquals(1, Distance.to(ISuper1.class).from(ISuper2.class));
        assertEquals(1, Distance.to(ISuper1.class).from(Super.class));
        assertEquals(3, Distance.to(ISuper3.class).from(Inner.class));
    }

    @Test(expected = IllegalStateException.class)
    public void distanceReverse() {
        fail("Should fail but was " + Distance.to(Inner.class).from(Super.class));
    }

    @Test(expected = IllegalStateException.class)
    public void distanceNonRelated() {
        fail("Should fail but was " + Distance.to(StringBuilder.class).from(List.class));
    }

    @Test(expected = NullPointerException.class)
    public void distanceAnyNull() {
        fail("Should fail but was " + Distance.to(null).from(List.class));
    }

    @Test(expected = NullPointerException.class)
    public void distanceNullAny() {
        fail("Should fail but was " + Distance.to(List.class).from(null));
    }

    @Test(expected = NullPointerException.class)
    public void distanceNullNull() {
      fail("Should fail but was " + Distance.to(null).from(null));
    }

    private static class Inner extends Super implements ISuper2 {
    }

    private static class Super extends Base implements ISuper1 {
    }

    private static class Base implements ISuper3 {
    }

    private interface ISuper1 {
    }

    private interface ISuper2 extends ISuper1 {
    }

    private interface ISuper3 {
    }
}
