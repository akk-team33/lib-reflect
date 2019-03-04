package de.team33.test.reflect.v3;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.team33.libs.reflect.v3.Classes;
import org.junit.Test;


public class ClassesTest
{

  @Test
  public void distance()
  {
    assertEquals(0, Classes.distance(Inner.class, Inner.class));
    assertEquals(1, Classes.distance(Inner.class, Super.class));
    assertEquals(2, Classes.distance(Inner.class, Object.class));
    assertEquals(1, Classes.distance(Inner.class, ISuper1.class));
    assertEquals(1, Classes.distance(Inner.class, ISuper2.class));
    assertEquals(-2, Classes.distance(Collection.class, Map.class));
  }

  @Test
  public void empty()
  {
    Stream.<Function<Class<?>, Stream<Class<?>>>>of(Classes::flat,
                                                    Classes::deep,
                                                    Classes::wide).forEach(toStream -> assertEquals(
      emptyList(),
      toStream.apply(null).map(Class::toString).collect(Collectors.toList())));
  }

  @Test
  public void flat()
  {
    assertEquals(singletonList("class de.team33.test.reflect.v3.ClassesTest$Inner"),
                 Classes.flat(Inner.class).map(Class::toString).collect(Collectors.toList()));
  }

  @Test
  public void deep()
  {
    assertEquals(Arrays.asList("class java.lang.Object",
                               "class de.team33.test.reflect.v3.ClassesTest$Super",
                               "class de.team33.test.reflect.v3.ClassesTest$Inner"),
                 Classes.deep(Inner.class).map(Class::toString).collect(Collectors.toList()));
  }

  @Test
  public void wide()
  {
    assertEquals(Arrays.asList("interface de.team33.test.reflect.v3.ClassesTest$ISuper1",
                               "interface de.team33.test.reflect.v3.ClassesTest$ISuper2",
                               "class java.lang.Object",
                               "class de.team33.test.reflect.v3.ClassesTest$Super",
                               "class de.team33.test.reflect.v3.ClassesTest$Inner"),
                 Classes.wide(Inner.class).map(Class::toString).collect(Collectors.toList()));
  }

  private static class Inner extends Super implements ISuper1, ISuper2
  {

  }


  private interface ISuper1
  {

  }


  private interface ISuper2 extends ISuper1
  {

  }


  private static class Super implements ISuper2
  {

  }
}
