package de.team33.test.reflect.v3;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import de.team33.libs.reflect.v3.Fields;
import org.junit.Test;


public class FieldsTest
{

  @Test
  public void flat()
  {
    assertEquals(
      Arrays.asList(
        "private static final int de.team33.test.reflect.v3.FieldsTest$Inner.privateStaticFinalInt",
        "private static int de.team33.test.reflect.v3.FieldsTest$Inner.privateStaticInt",
        "private final int de.team33.test.reflect.v3.FieldsTest$Inner.privateFinalInt",
        "private int de.team33.test.reflect.v3.FieldsTest$Inner.privateInt"),
      Fields.flat(Inner.class).map(Field::toString).collect(Collectors.toList())
    );
  }

  @Test
  public void deep()
  {
    assertEquals(
      Arrays.asList(
        "private static final int de.team33.test.reflect.v3.FieldsTest$Super.privateStaticFinalInt",
        "private static int de.team33.test.reflect.v3.FieldsTest$Super.privateStaticInt",
        "private final int de.team33.test.reflect.v3.FieldsTest$Super.privateFinalInt",
        "private int de.team33.test.reflect.v3.FieldsTest$Super.privateInt",
        "private static final int de.team33.test.reflect.v3.FieldsTest$Inner.privateStaticFinalInt",
        "private static int de.team33.test.reflect.v3.FieldsTest$Inner.privateStaticInt",
        "private final int de.team33.test.reflect.v3.FieldsTest$Inner.privateFinalInt",
        "private int de.team33.test.reflect.v3.FieldsTest$Inner.privateInt"),
      Fields.deep(Inner.class).map(Field::toString).collect(Collectors.toList())
    );
  }

  @Test
  public void wide()
  {
    assertEquals(
      Arrays.asList(
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper1.privateStaticFinalInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper1.privateStaticInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper1.privateFinalInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper1.privateInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper2.privateStaticFinalInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper2.privateStaticInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper2.privateFinalInt",
        "public static final int de.team33.test.reflect.v3.FieldsTest$ISuper2.privateInt",
        "private static final int de.team33.test.reflect.v3.FieldsTest$Super.privateStaticFinalInt",
        "private static int de.team33.test.reflect.v3.FieldsTest$Super.privateStaticInt",
        "private final int de.team33.test.reflect.v3.FieldsTest$Super.privateFinalInt",
        "private int de.team33.test.reflect.v3.FieldsTest$Super.privateInt",
        "private static final int de.team33.test.reflect.v3.FieldsTest$Inner.privateStaticFinalInt",
        "private static int de.team33.test.reflect.v3.FieldsTest$Inner.privateStaticInt",
        "private final int de.team33.test.reflect.v3.FieldsTest$Inner.privateFinalInt",
        "private int de.team33.test.reflect.v3.FieldsTest$Inner.privateInt"),
      Fields.wide(Inner.class).map(Field::toString).collect(Collectors.toList())
    );
  }

  @Test
  public void namingSimple()
  {
    assertEquals(
      Arrays.asList(
        "privateStaticFinalInt",
        "privateStaticInt",
        "privateFinalInt",
        "privateInt",
        "privateStaticFinalInt",
        "privateStaticInt",
        "privateFinalInt",
        "privateInt"),
      Fields.deep(Inner.class).map(Fields.Naming.SIMPLE).collect(Collectors.toList())
    );
  }

  @Test
  public void namingFullQualified()
  {
    assertEquals(
      Arrays.asList(
        "de.team33.test.reflect.v3.FieldsTest.Super.privateStaticFinalInt",
        "de.team33.test.reflect.v3.FieldsTest.Super.privateStaticInt",
        "de.team33.test.reflect.v3.FieldsTest.Super.privateFinalInt",
        "de.team33.test.reflect.v3.FieldsTest.Super.privateInt",
        "de.team33.test.reflect.v3.FieldsTest.Inner.privateStaticFinalInt",
        "de.team33.test.reflect.v3.FieldsTest.Inner.privateStaticInt",
        "de.team33.test.reflect.v3.FieldsTest.Inner.privateFinalInt",
        "de.team33.test.reflect.v3.FieldsTest.Inner.privateInt"),
      Fields.deep(Inner.class).map(Fields.Naming.FULL_QUALIFIED).collect(Collectors.toList())
    );
  }

  private static class Inner extends Super implements ISuper1, ISuper2
  {

    private static final int privateStaticFinalInt = 0;

    private static int privateStaticInt;

    private final int privateFinalInt;

    private int privateInt;

    private Inner(final int privateFinalInt, final int privateFinalSuperInt)
    {
      super(privateFinalSuperInt);
      this.privateFinalInt = privateFinalInt;
    }
  }


  private interface ISuper1
  {

    static final int privateStaticFinalInt = 0;

    static int privateStaticInt = 0;

    final int privateFinalInt = 0;

    int privateInt = 0;
  }

  private interface ISuper2 extends ISuper1
  {

    static final int privateStaticFinalInt = 0;

    static int privateStaticInt = 0;

    final int privateFinalInt = 0;

    int privateInt = 0;
  }

  private static class Super //implements ISuper2
  {

    private static final int privateStaticFinalInt = 0;

    private static int privateStaticInt;

    private final int privateFinalInt;

    private int privateInt;

    private Super(final int privateFinalInt)
    {
      this.privateFinalInt = privateFinalInt;
    }
  }
}
