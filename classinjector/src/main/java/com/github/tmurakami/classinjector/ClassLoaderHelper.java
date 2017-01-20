package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
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

    final ClassLoader getParent(final ClassLoader classLoader) {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return classLoader.getParent();
            }
        });
    }

    private static ClassLoaderHelper newInstance() {
        Field parentField = ReflectionUtils.getDeclaredField(ClassLoader.class, "parent");
        try {
            parentField.setAccessible(true);
            return ReflectionClassLoaderHelper.create(parentField);
        } catch (RuntimeException e) {
            if ("java.lang.reflect.InaccessibleObjectException".equals(e.getClass().getName())) {
                return UnsafeClassLoaderHelper.create(parentField);
            } else {
                throw e;
            }
        }
    }

}
