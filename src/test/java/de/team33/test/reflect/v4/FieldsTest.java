package de.team33.test.reflect.v4;

import de.team33.libs.reflect.v4.Fields;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FieldsTest {

    @Test
    public void flat() {
        assertEquals(
                Arrays.asList(
                        "private static final int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticFinalInt",
                        "private static int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticInt",
                        "private final int de.team33.test.reflect.v4.FieldsTest$Inner.privateFinalInt",
                        "private int de.team33.test.reflect.v4.FieldsTest$Inner.privateInt"),
                Fields.flatStreamOf(Inner.class).map(Field::toString).collect(Collectors.toList())
        );
    }

    @Test
    public void deep() {
        assertEquals(
                Arrays.asList(
                        "private static final int de.team33.test.reflect.v4.FieldsTest$Super.privateStaticFinalInt",
                        "private static int de.team33.test.reflect.v4.FieldsTest$Super.privateStaticInt",
                        "private final int de.team33.test.reflect.v4.FieldsTest$Super.privateFinalInt",
                        "private int de.team33.test.reflect.v4.FieldsTest$Super.privateInt",
                        "private static final int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticFinalInt",
                        "private static int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticInt",
                        "private final int de.team33.test.reflect.v4.FieldsTest$Inner.privateFinalInt",
                        "private int de.team33.test.reflect.v4.FieldsTest$Inner.privateInt"),
                Fields.deepStreamOf(Inner.class).map(Field::toString).collect(Collectors.toList())
        );
    }

    @Test
    public void wide() {
        assertEquals(
                Arrays.asList(
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper1.privateStaticFinalInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper1.privateStaticInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper1.privateFinalInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper1.privateInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper2.privateStaticFinalInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper2.privateStaticInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper2.privateFinalInt",
                        "public static final int de.team33.test.reflect.v4.FieldsTest$ISuper2.privateInt",
                        "private static final int de.team33.test.reflect.v4.FieldsTest$Super.privateStaticFinalInt",
                        "private static int de.team33.test.reflect.v4.FieldsTest$Super.privateStaticInt",
                        "private final int de.team33.test.reflect.v4.FieldsTest$Super.privateFinalInt",
                        "private int de.team33.test.reflect.v4.FieldsTest$Super.privateInt",
                        "private static final int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticFinalInt",
                        "private static int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticInt",
                        "private final int de.team33.test.reflect.v4.FieldsTest$Inner.privateFinalInt",
                        "private int de.team33.test.reflect.v4.FieldsTest$Inner.privateInt"),
                Fields.wideStreamOf(Inner.class).map(Field::toString).collect(Collectors.toList())
        );
    }

    @Test
    public void namingSimple() {
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
                Fields.deepStreamOf(Inner.class).map(Fields.Naming.SIMPLE).collect(Collectors.toList())
        );
    }

    @Test
    public void namingCanonical() {
        assertEquals(
                Arrays.asList(
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateInt"),
                Fields.deepStreamOf(Inner.class).map(Fields.Naming.CANONICAL).collect(Collectors.toList())
        );
    }

    @Test
    public void namingContextSensitiveQualified() {
        assertEquals(
                Arrays.asList(
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateInt",
                        "privateStaticFinalInt",
                        "privateStaticInt",
                        "privateFinalInt",
                        "privateInt"),
                Fields.deepStreamOf(Sub.class)
                        .map(Fields.Naming.conditional(Sub.class))
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void namingContextSensitiveCompact() {
        assertEquals(
                Arrays.asList(
                        "..privateStaticFinalInt",
                        "..privateStaticInt",
                        "..privateFinalInt",
                        "..privateInt",
                        ".privateStaticFinalInt",
                        ".privateStaticInt",
                        ".privateFinalInt",
                        ".privateInt",
                        "privateStaticFinalInt",
                        "privateStaticInt",
                        "privateFinalInt",
                        "privateInt"),
                Fields.deepStreamOf(Sub.class)
                        .map(Fields.Naming.compact(Sub.class))
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void canonicalName() {
        Fields.flatStreamOf(Sub.class).forEach(field -> {
            final String canonicalName = Fields.canonicalName(field);
            assertTrue(canonicalName.startsWith(Sub.class.getCanonicalName()));
            assertTrue(canonicalName.endsWith(field.getName()));
        });
    }

    interface ISuper1 {

        int privateStaticFinalInt = 0;

        int privateStaticInt = 0;

        int privateFinalInt = 0;

        int privateInt = 0;
    }


    interface ISuper2 extends ISuper1 {

        int privateStaticFinalInt = 0;

        int privateStaticInt = 0;

        int privateFinalInt = 0;

        int privateInt = 0;
    }

    static class Inner extends Super implements ISuper1, ISuper2 {

        private static final int privateStaticFinalInt = 0;

        private static int privateStaticInt;

        private final int privateFinalInt;

        private int privateInt;

        private Inner(final int privateFinalInt, final int privateFinalSuperInt) {
            super(privateFinalSuperInt);
            this.privateFinalInt = privateFinalInt;
        }
    }

    static class Sub extends Inner implements ISuper1, ISuper2 {

        private static final int privateStaticFinalInt = 0;

        private static int privateStaticInt;

        private final int privateFinalInt;

        private int privateInt;

        private Sub(final int privateFinalInt, final int privateFinalInnerInt, final int privateFinalSuperInt) {
            super(privateFinalInnerInt, privateFinalSuperInt);
            this.privateFinalInt = privateFinalInt;
        }
    }

    static class Super {

        private static final int privateStaticFinalInt = 0;

        private static int privateStaticInt;

        private final int privateFinalInt;

        private int privateInt;

        private Super(final int privateFinalInt) {
            this.privateFinalInt = privateFinalInt;
        }
    }
}
