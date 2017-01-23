package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.net.URL;
import java.security.ProtectionDomain;

abstract class ClassLoaderHelper {

    static final ClassLoaderHelper INSTANCE;

    static {
        try {
            INSTANCE = newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("This Java version is not supported", e);
        }
    }

    ClassLoaderHelper() {
    }

    abstract Class defineClass(ClassLoader classLoader,
                               String name,
                               byte[] b,
                               int off,
                               int len,
                               ProtectionDomain protectionDomain) throws ClassFormatError;

    abstract Package definePackage(ClassLoader classLoader,
                                   String name,
                                   String specTitle,
                                   String specVersion,
                                   String specVendor,
                                   String implTitle,
                                   String implVersion,
                                   String implVendor,
                                   URL sealBase) throws IllegalArgumentException;

    abstract Package getPackage(ClassLoader classLoader, String name);

    abstract void setParent(ClassLoader classLoader, ClassLoader parent);

    private static ClassLoaderHelper newInstance() throws Exception {
        Field parentField = ClassLoader.class.getDeclaredField("parent");
        try {
            parentField.setAccessible(true);
            return ReflectionClassLoaderHelper.create(parentField);
        } catch (RuntimeException e) {
            if ("java.lang.reflect.InaccessibleObjectException".equals(e.getClass().getName())) {
                try {
                    Class<?> c = Class.forName("sun.misc.Unsafe");
                    Field f = c.getDeclaredField("theUnsafe");
                    f.setAccessible(true);
                    Object unsafe = f.get(null);
                    return new UnsafeClassLoaderHelper(parentField, Unsafe.wrap(unsafe));
                } catch (Throwable ignored) {
                }
            }
            throw e;
        }
    }

}
