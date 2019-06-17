package de.team33.test.reflect.v4;

import static de.team33.libs.reflect.v4.Methods.Streaming.PUBLIC_GETTERS;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import de.team33.libs.reflect.v4.Methods;
import de.team33.test.reflect.common.Level3;
import org.junit.Test;


public class MethodsStreamingTest
{

  @Test
  public void publicGetters()
  {
    assertEquals(
      new TreeSet<>(Arrays.asList(
        "public final int de.team33.test.reflect.common.Level3.getPrivateFinalInt()",
        "public final java.util.Date de.team33.test.reflect.common.Level3.getPrivateFinalDate()",
        "public final java.lang.String de.team33.test.reflect.common.Level3.getPrivateFinalString()",
        "public final java.util.Map de.team33.test.reflect.common.Level2.getPrivateFinalMapOfStringToInteger()",
        "public final long de.team33.test.reflect.common.Level2.getPrivateFinalLong()",
        "public final java.util.List de.team33.test.reflect.common.Level2.getPrivateFinalListOfString()",
        "public java.lang.Boolean de.team33.test.reflect.common.Level1.getPrivateFinalBoolean()",
        "public java.util.Set de.team33.test.reflect.common.Level1.getPrivateFinalSetOfInteger()")),
      PUBLIC_GETTERS.apply(Level3.class)
                    .map(Method::toString)
                    .collect(TreeSet::new, Set::add, Set::addAll)
    );
    assertEquals(7L, new Level3(1, "a", new Date()).getPrivateFinalLong());
  }
}
