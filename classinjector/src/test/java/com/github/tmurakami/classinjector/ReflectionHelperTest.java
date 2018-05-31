package com.github.tmurakami.classinjector;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ReflectionHelperTest {

    private final ReflectionHelper reflectionHelper = new ReflectionHelper();

    @Test
    public void get_should_simply_call_Field_get() throws Exception {
        Field f = C.class.getDeclaredField("value");
        f.setAccessible(true);
        assertEquals("0", reflectionHelper.get(f, new C("0")));
    }

    @Test
    public void getDeclaredField_should_simply_call_Class_getDeclaredField() throws Exception {
        Field f = reflectionHelper.getDeclaredField(C.class, "value");
        assertSame(C.class, f.getDeclaringClass());
        assertEquals("value", f.getName());
    }

    @Test
    public void getDeclaredMethod_should_simply_call_Class_getDeclaredMethod() throws Exception {
        Method m = reflectionHelper.getDeclaredMethod(C.class, "doIt", String.class);
        assertSame(C.class, m.getDeclaringClass());
        assertEquals("doIt", m.getName());
    }

    @Test
    public void invoke_should_simply_call_Method_invoke() throws Exception {
        Method m = C.class.getDeclaredMethod("doIt", String.class);
        m.setAccessible(true);
        assertEquals("01", reflectionHelper.invoke(m, new C("0"), "1"));
    }

    @Test
    public void set_should_simply_call_Field_set() throws Exception {
        Field f = C.class.getDeclaredField("value");
        f.setAccessible(true);
        C c = new C("0");
        reflectionHelper.set(f, c, "1");
        assertEquals("1", c.value);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void setAccessible_should_simply_call_AccessibleObject_setAccessible() throws Exception {
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