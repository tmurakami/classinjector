package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

final class UnsafeClassLoaderHelper extends ClassLoaderHelper {

    private final Field parentField;
    private final UnsafeWrapper unsafeWrapper;

    UnsafeClassLoaderHelper(Field parentField, UnsafeWrapper unsafeWrapper) {
        this.parentField = parentField;
        this.unsafeWrapper = unsafeWrapper;
    }

    @Override
    Class defineClass(ClassLoader classLoader,
                      String name,
                      byte[] b,
                      int off,
                      int len,
                      ProtectionDomain protectionDomain) throws ClassFormatError {
        return unsafeWrapper.defineClass(name, b, off, len, classLoader, protectionDomain);
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        UnsafeWrapper u = unsafeWrapper;
        u.putObject(classLoader, u.objectFieldOffset(parentField), parent);
    }

}
