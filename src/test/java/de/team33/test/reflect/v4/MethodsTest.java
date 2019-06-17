package de.team33.test.reflect.v4;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import de.team33.libs.reflect.v4.Methods;
import de.team33.test.reflect.common.Level3;
import org.junit.Test;


public class MethodsTest
{

  @Test
  public void publicStreamOf()
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
        "public java.util.Set de.team33.test.reflect.common.Level1.getPrivateFinalSetOfInteger()",
        "public final void java.lang.Object.wait() throws java.lang.InterruptedException",
        "public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException",
        "public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException",
        "public boolean java.lang.Object.equals(java.lang.Object)",
        "public java.lang.String java.lang.Object.toString()",
        "public native int java.lang.Object.hashCode()",
        "public final native java.lang.Class java.lang.Object.getClass()",
        "public final native void java.lang.Object.notify()",
        "public final native void java.lang.Object.notifyAll()")),
      Methods.publicStreamOf(Level3.class).map(Method::toString).collect(TreeSet::new, Set::add, Set::addAll)
    );
    assertEquals(7L, new Level3(1, "a", new Date()).getPrivateFinalLong());
  }
}
