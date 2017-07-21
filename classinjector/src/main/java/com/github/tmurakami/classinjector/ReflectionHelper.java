package com.github.tmurakami.classinjector;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class ReflectionHelper {

    Object get(Field f, Object o) throws IllegalAccessException {
        return f.get(o);
    }

    Field getDeclaredField(Class<?> c, String name) throws NoSuchFieldException {
        return c.getDeclaredField(name);
    }

    Method getDeclaredMethod(Class<?> c, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        return c.getDeclaredMethod(name, parameterTypes);
    }

    Object invoke(Method m, Object o, Object... parameters)
            throws InvocationTargetException, IllegalAccessException {
        return m.invoke(o, parameters);
    }

    void set(Field f, Object o, Object value) throws IllegalAccessException {
        f.set(o, value);
    }

    void setAccessible(AccessibleObject o,
                       @SuppressWarnings("SameParameterValue") boolean accessible) {
        o.setAccessible(accessible);
    }

}
