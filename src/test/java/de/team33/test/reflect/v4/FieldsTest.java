package de.team33.test.reflect.v4;

import de.team33.libs.reflect.v4.Fields;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


public class FieldsTest {

    @Test
    public void flat() {
        assertEquals(
                Arrays.asList(
                        "private static final int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticFinalInt",
                        "private static int de.team33.test.reflect.v4.FieldsTest$Inner.privateStaticInt",
                        "private final int de.team33.test.reflect.v4.FieldsTest$Inner.privateFinalInt",
                        "private int de.team33.test.reflect.v4.FieldsTest$Inner.privateInt"),
                Fields.flat(Inner.class).map(Field::toString).collect(Collectors.toList())
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
                Fields.deep(Inner.class).map(Field::toString).collect(Collectors.toList())
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
                Fields.wide(Inner.class).map(Field::toString).collect(Collectors.toList())
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
                Fields.deep(Inner.class).map(Fields.Naming.SIMPLE).collect(Collectors.toList())
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
                Fields.deep(Inner.class).map(Fields.Naming.CANONICAL).collect(Collectors.toList())
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
                Fields.deep(Sub.class)
                        .map(Fields.Naming.Hierarchical.QUALIFIED.apply(Sub.class))
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
                Fields.deep(Sub.class)
                        .map(Fields.Naming.Hierarchical.COMPACT.apply(Sub.class))
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void mapperDeep() {
        final Fields.Mapping mapper = Fields.mapping()
                .setToFieldStream(Fields.Streaming.DEEP)
                .setToNaming(Fields.Naming.Hierarchical.COMPACT)
                .build();
        assertEquals(
                Arrays.asList(
                        "..privateFinalInt",
                        "..privateInt",
                        "..privateStaticFinalInt",
                        "..privateStaticInt",
                        ".privateFinalInt",
                        ".privateInt",
                        ".privateStaticFinalInt",
                        ".privateStaticInt",
                        "privateFinalInt",
                        "privateInt",
                        "privateStaticFinalInt",
                        "privateStaticInt"),
                new ArrayList<>(mapper.apply(Sub.class).keySet())
        );
    }

    @Test
    public void mapperWide() {
        final Fields.Mapping mapper = Fields.mapping()
                .setToFieldStream(Fields.Streaming.WIDE)
                .setToName(Fields.Naming.CANONICAL)
                .build();
        assertEquals(
                Arrays.asList(
                        "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Sub.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Sub.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.Sub.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Sub.privateStaticInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticFinalInt",
                        "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticInt"),
                new ArrayList<>(mapper.apply(Sub.class).keySet())
        );
    }

    private interface ISuper1 {

        int privateStaticFinalInt = 0;

        int privateStaticInt = 0;

        int privateFinalInt = 0;

        int privateInt = 0;
    }


    private interface ISuper2 extends ISuper1 {

        int privateStaticFinalInt = 0;

        int privateStaticInt = 0;

        int privateFinalInt = 0;

        int privateInt = 0;
    }

    private static class Inner extends Super implements ISuper1, ISuper2 {

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

    private static class Super {

        private static final int privateStaticFinalInt = 0;

        private static int privateStaticInt;

        private final int privateFinalInt;

        private int privateInt;

        private Super(final int privateFinalInt) {
            this.privateFinalInt = privateFinalInt;
        }
    }
}
