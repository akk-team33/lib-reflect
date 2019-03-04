package de.team33.test.reflect.v3;

import de.team33.libs.reflect.v3.Classes;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ClassesTest {

    @Test
    public void distance() {
        assertEquals(0, Classes.distance(Inner.class, Inner.class));
        assertEquals(1, Classes.distance(Inner.class, Super.class));
        assertEquals(2, Classes.distance(Inner.class, Base.class));
        assertEquals(3, Classes.distance(Inner.class, Object.class));
        assertEquals(2, Classes.distance(Inner.class, ISuper1.class));
        assertEquals(1, Classes.distance(Inner.class, ISuper2.class));
        assertEquals(1, Classes.distance(ISuper2.class, ISuper1.class));
        assertEquals(1, Classes.distance(Super.class, ISuper1.class));
        assertEquals(3, Classes.distance(Inner.class, ISuper3.class));
    }

    @Test(expected = IllegalStateException.class)
    public void distanceReverse() {
        fail("Should fail but was " + Classes.distance(Super.class, Inner.class));
    }

    @Test(expected = IllegalStateException.class)
    public void distanceNonRelated() {
        fail("Should fail but was " + Classes.distance(List.class, StringBuilder.class));
    }

    @Test(expected = NullPointerException.class)
    public void distanceAnyNull() {
        fail("Should fail but was " + Classes.distance(List.class, null));
    }

    @Test(expected = NullPointerException.class)
    public void distanceNullAny() {
        fail("Should fail but was " + Classes.distance(null, List.class));
    }

    @Test
    public void distanceNullNull() {
        assertEquals(0, Classes.distance(null, null));
    }

    @Test
    public void empty() {
        Stream.<Function<Class<?>, Stream<Class<?>>>>of(Classes::flat,
                Classes::deep,
                Classes::wide).forEach(toStream -> assertEquals(
                emptyList(),
                toStream.apply(null).map(Class::toString).collect(Collectors.toList())));
    }

    @Test
    public void flat() {
        assertEquals(singletonList("class de.team33.test.reflect.v3.ClassesTest$Inner"),
                Classes.flat(Inner.class).map(Class::toString).collect(Collectors.toList()));
    }

    @Test
    public void deep() {
        assertEquals(
                Arrays.asList(
                        "class java.lang.Object",
                        "class de.team33.test.reflect.v3.ClassesTest$Base",
                        "class de.team33.test.reflect.v3.ClassesTest$Super",
                        "class de.team33.test.reflect.v3.ClassesTest$Inner"
                ),
                Classes.deep(Inner.class).map(Class::toString).collect(Collectors.toList()));
    }

    @Test
    public void wide() {
        assertEquals(Arrays.asList("interface de.team33.test.reflect.v3.ClassesTest$ISuper1",
                "interface de.team33.test.reflect.v3.ClassesTest$ISuper2",
                "interface de.team33.test.reflect.v3.ClassesTest$ISuper3",
                "class java.lang.Object",
                "class de.team33.test.reflect.v3.ClassesTest$Base",
                "class de.team33.test.reflect.v3.ClassesTest$Super",
                "class de.team33.test.reflect.v3.ClassesTest$Inner"),
                Classes.wide(Inner.class).map(Class::toString).collect(Collectors.toList()));
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
