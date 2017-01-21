package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.net.URL;
import java.security.ProtectionDomain;

final class UnsafeClassLoaderHelper extends ClassLoaderHelper {

    private final Field parentField;
    private final Unsafe unsafe;

    UnsafeClassLoaderHelper(Field parentField, Unsafe unsafe) {
        this.parentField = parentField;
        this.unsafe = unsafe;
    }

    @Override
    Class defineClass(ClassLoader classLoader,
                      String name,
                      byte[] b,
                      int off,
                      int len,
                      ProtectionDomain protectionDomain) throws ClassFormatError {
        return unsafe.defineClass(name, b, off, len, classLoader, protectionDomain);
    }

    @Override
    Package definePackage(ClassLoader classLoader,
                          String name,
                          String specTitle,
                          String specVersion,
                          String specVendor,
                          String implTitle,
                          String implVersion,
                          String implVendor,
                          URL sealBase) throws IllegalArgumentException {
        return null;
    }

    @Override
    Package getPackage(ClassLoader classLoader, String name) {
        return null;
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        Unsafe u = unsafe;
        u.putObject(classLoader, u.objectFieldOffset(parentField), parent);
    }

}
