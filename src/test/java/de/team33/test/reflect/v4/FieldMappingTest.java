package de.team33.test.reflect.v4;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

import de.team33.libs.reflect.v4.FieldMapping;
import de.team33.libs.reflect.v4.Fields;
import org.junit.Test;


public class FieldMappingTest
{

  @Test
  public void significantFlat()
  {
    final Map<String, Field> result = FieldMapping.SIGNIFICANT_FLAT.apply(FieldsTest.Sub.class);
    assertEquals(Arrays.asList("privateFinalInt", "privateInt"),
                 new ArrayList<>(new TreeSet<>(result.keySet())));
  }

  @Test
  public void significantDeep()
  {
    final Map<String, Field> result = FieldMapping.SIGNIFICANT_DEEP.apply(FieldsTest.Sub.class);
    assertEquals(Arrays.asList("..privateFinalInt",
                               "..privateInt",
                               ".privateFinalInt",
                               ".privateInt",
                               "privateFinalInt",
                               "privateInt"), new ArrayList<>(new TreeSet<>(result.keySet())));
  }

  @Test
  public void allWide()
  {
    final Map<String, Field> result = new FieldMapping(Fields.Streaming.WIDE,
                                                       Fields.Naming.Hierarchical.QUALIFIED).apply(FieldsTest.Sub.class);
    assertEquals(Arrays.asList("de.team33.test.reflect.v4.FieldsTest.ISuper1.privateFinalInt",
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
                               "de.team33.test.reflect.v4.FieldsTest.Super.privateFinalInt",
                               "de.team33.test.reflect.v4.FieldsTest.Super.privateInt",
                               "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticFinalInt",
                               "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticInt",
                               "privateFinalInt",
                               "privateInt",
                               "privateStaticFinalInt",
                               "privateStaticInt"), new ArrayList<>(new TreeSet<>(result.keySet())));
  }
}
