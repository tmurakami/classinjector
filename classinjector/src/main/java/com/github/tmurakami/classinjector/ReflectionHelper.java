package com.github.tmurakami.classinjector;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class ReflectionHelper {

    static final ReflectionHelper INSTANCE = new ReflectionHelper();

    private ReflectionHelper() {
    }

    Field getDeclaredField(Class<?> c, String name) {
        try {
            return c.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldError(e.getMessage());
        }
    }

    Method getDeclaredMethod(Class<?> c, String name, Class<?>... parameterTypes) {
        try {
            return c.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    Method getMethod(Class<?> c, String name, Class<?>... parameterTypes) {
        try {
            return c.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    Object get(Field field, Object o) {
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    Object invoke(Method method, Object o, Object... parameters) {
        try {
            return method.invoke(o, parameters);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new IllegalStateException("Cannot invoke "
                        + method.getDeclaringClass().getName() + '#' + method.getName(), cause);
            }
        }
    }

    void set(Field field, Object o, Object value) {
        try {
            field.set(o, value);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    void setAccessible(AccessibleObject o, boolean accessible) {
        o.setAccessible(accessible);
    }

}
