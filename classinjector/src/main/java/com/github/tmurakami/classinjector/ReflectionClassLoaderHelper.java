package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

final class ReflectionClassLoaderHelper extends ClassLoaderHelper {

    private final Field parentField;
    private final Method defineClassMethod;

    ReflectionClassLoaderHelper(Field parentField, Method defineClassMethod) {
        this.parentField = parentField;
        this.defineClassMethod = defineClassMethod;
    }

    @Override
    Class defineClass(ClassLoader classLoader,
                      String name,
                      byte[] b,
                      int off,
                      int len,
                      ProtectionDomain protectionDomain) throws ClassFormatError {
        Method m = defineClassMethod;
        m.setAccessible(true);
        return (Class) ReflectionUtils.invoke(m, classLoader, name, b, off, len, protectionDomain);
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        Field f = this.parentField;
        f.setAccessible(true);
        ReflectionUtils.set(f, classLoader, parent);
    }

}
