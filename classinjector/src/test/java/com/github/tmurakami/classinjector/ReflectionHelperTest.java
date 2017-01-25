package com.github.tmurakami.classinjector;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ReflectionHelperTest {

    private final ReflectionHelper reflectionHelper = ReflectionHelper.INSTANCE;

    @Test
    public void get() throws Exception {
        Field f = C.class.getDeclaredField("value");
        f.setAccessible(true);
        assertEquals("0", reflectionHelper.get(f, new C("0")));
    }

    @Test
    public void getDeclaredField() throws Exception {
        Field f = reflectionHelper.getDeclaredField(C.class, "value");
        assertSame(C.class, f.getDeclaringClass());
        assertEquals("value", f.getName());
    }

    @Test
    public void getDeclaredMethod() throws Exception {
        Method m = reflectionHelper.getDeclaredMethod(C.class, "doIt", String.class);
        assertSame(C.class, m.getDeclaringClass());
        assertEquals("doIt", m.getName());
    }

    @Test
    public void invoke() throws Exception {
        Method m = C.class.getDeclaredMethod("doIt", String.class);
        m.setAccessible(true);
        assertEquals("01", reflectionHelper.invoke(m, new C("0"), "1"));
    }

    @Test
    public void set() throws Exception {
        Field f = C.class.getDeclaredField("value");
        f.setAccessible(true);
        C c = new C("0");
        reflectionHelper.set(f, c, "1");
        assertEquals("1", c.value);
    }

    @Test
    public void setAccessible() throws Exception {
        Field f = C.class.getDeclaredField("value");
        assertFalse(f.isAccessible());
        reflectionHelper.setAccessible(f, true);
        assertTrue(f.isAccessible());
    }

    private static class C {

        final String value;

        C(String value) {
            this.value = value;
        }

        @SuppressWarnings("unused")
        String doIt(String s) {
            return value + s;
        }

    }

}