package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

abstract class ClassLoaderHelper {

    static final ClassLoaderHelper INSTANCE = newInstance();

    ClassLoaderHelper() {
    }

    abstract Class defineClass(ClassLoader classLoader,
                               String name,
                               byte[] b,
                               int off,
                               int len,
                               ProtectionDomain protectionDomain) throws ClassFormatError;

    abstract void setParent(ClassLoader classLoader, ClassLoader parent);

    private static ClassLoaderHelper newInstance() {
        Class<?> c = ClassLoader.class;
        Field parentField = ReflectionUtils.getDeclaredField(c, "parent");
        try {
            parentField.setAccessible(true);
            Method defineClassMethod = ReflectionUtils.getDeclaredMethod(
                    c,
                    "defineClass",
                    String.class,
                    byte[].class,
                    int.class,
                    int.class,
                    ProtectionDomain.class);
            return new ReflectionClassLoaderHelper(parentField, defineClassMethod);
        } catch (RuntimeException e) {
            if ("java.lang.reflect.InaccessibleObjectException".equals(e.getClass().getName())) {
                return new UnsafeClassLoaderHelper(parentField, Unsafe.getUnsafe());
            }
            throw e;
        }
    }

}
