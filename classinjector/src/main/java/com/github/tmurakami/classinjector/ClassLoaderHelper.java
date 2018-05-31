package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

abstract class ClassLoaderHelper {

    static final ClassLoaderHelper INSTANCE;

    static {
        try {
            INSTANCE = newInstance(new ReflectionHelper());
        } catch (Exception e) {
            throw new IllegalStateException("This Java version is not supported", e);
        }
    }

    ClassLoaderHelper() {
    }

    abstract Class defineClass(ClassLoader classLoader,
                               String name,
                               byte[] b,
                               @SuppressWarnings("SameParameterValue") int off,
                               int len,
                               ProtectionDomain protectionDomain) throws ClassFormatError;

    abstract void setParent(ClassLoader classLoader, ClassLoader parent);

    private static ClassLoaderHelper newInstance(ReflectionHelper reflectionHelper)
            throws Exception {
        Field parentField = reflectionHelper.getDeclaredField(ClassLoader.class, "parent");
        try {
            reflectionHelper.setAccessible(parentField, true);
        } catch (RuntimeException e) {
            if ("java.lang.reflect.InaccessibleObjectException".equals(e.getClass().getName())) {
                try {
                    Class<?> c = Class.forName("sun.misc.Unsafe");
                    Field f = reflectionHelper.getDeclaredField(c, "theUnsafe");
                    reflectionHelper.setAccessible(f, true);
                    Object unsafe = reflectionHelper.get(f, null);
                    return new UnsafeClassLoaderHelper(parentField, new UnsafeWrapper(unsafe));
                } catch (Exception ignored) {
                }
            }
            throw e;
        }
        return ReflectionClassLoaderHelper.newInstance(parentField, reflectionHelper);
    }

}
