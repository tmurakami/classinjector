package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

final class ReflectionClassLoaderHelper extends ClassLoaderHelper {

    private Field parentField;
    private Method defineClassMethod;
    private ReflectionHelper reflectionHelper;

    private ReflectionClassLoaderHelper() {
    }

    @Override
    Class defineClass(ClassLoader classLoader,
                      String name,
                      byte[] b,
                      int off,
                      int len,
                      ProtectionDomain protectionDomain) throws ClassFormatError {
        ReflectionHelper h = reflectionHelper;
        Method m = defineClassMethod;
        h.setAccessible(m, true);
        return (Class) invoke(h, m, classLoader, name, b, off, len, protectionDomain);
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        ReflectionHelper h = reflectionHelper;
        Field f = parentField;
        h.setAccessible(f, true);
        try {
            h.set(f, classLoader, parent);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    static ReflectionClassLoaderHelper newInstance(Field parentField,
                                                   ReflectionHelper reflectionHelper)
            throws NoSuchMethodException {
        Class<?> c = ClassLoader.class;
        ReflectionClassLoaderHelper o = new ReflectionClassLoaderHelper();
        o.parentField = parentField;
        o.defineClassMethod = reflectionHelper.getDeclaredMethod(c,
                                                                 "defineClass",
                                                                 String.class,
                                                                 byte[].class,
                                                                 int.class,
                                                                 int.class,
                                                                 ProtectionDomain.class);
        o.reflectionHelper = reflectionHelper;
        return o;
    }

    private static Object invoke(ReflectionHelper reflectionHelper,
                                 Method method,
                                 Object o, Object... parameters) {
        try {
            return reflectionHelper.invoke(method, o, parameters);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new IllegalStateException("Cannot invoke " + method.getDeclaringClass().getName() + '#' + method.getName(), cause);
            }
        }
    }

}
