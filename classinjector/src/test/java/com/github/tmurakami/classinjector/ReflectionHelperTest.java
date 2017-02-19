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
    public void the_get_method_should_simply_call_the_Field_get_method() throws Exception {
        Field f = C.class.getDeclaredField("value");
        f.setAccessible(true);
        assertEquals("0", reflectionHelper.get(f, new C("0")));
    }

    @Test
    public void the_getDeclaredField_method_should_simply_call_the_Class_getDeclaredField_method() throws Exception {
        Field f = reflectionHelper.getDeclaredField(C.class, "value");
        assertSame(C.class, f.getDeclaringClass());
        assertEquals("value", f.getName());
    }

    @Test
    public void the_getDeclaredMethod_method_should_simply_call_the_Class_getDeclaredMethod_method() throws Exception {
        Method m = reflectionHelper.getDeclaredMethod(C.class, "doIt", String.class);
        assertSame(C.class, m.getDeclaringClass());
        assertEquals("doIt", m.getName());
    }

    @Test
    public void the_invoke_method_should_simply_call_the_Method_invoke_method() throws Exception {
        Method m = C.class.getDeclaredMethod("doIt", String.class);
        m.setAccessible(true);
        assertEquals("01", reflectionHelper.invoke(m, new C("0"), "1"));
    }

    @Test
    public void the_set_method_should_simply_call_the_Field_set_method() throws Exception {
        Field f = C.class.getDeclaredField("value");
        f.setAccessible(true);
        C c = new C("0");
        reflectionHelper.set(f, c, "1");
        assertEquals("1", c.value);
    }

    @Test
    public void the_setAccessible_method_should_simply_call_the_AccessibleObject_setAccessible_method() throws Exception {
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