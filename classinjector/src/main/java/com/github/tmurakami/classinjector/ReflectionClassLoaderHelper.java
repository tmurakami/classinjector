package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;

final class ReflectionClassLoaderHelper extends ClassLoaderHelper {

    private final Field parentField;
    private final Method defineClassMethod;
    private final Method definePackageMethod;
    private final Method getPackageMethod;

    private ReflectionClassLoaderHelper(Field parentField,
                                        Method defineClassMethod,
                                        Method definePackageMethod,
                                        Method getPackageMethod) {
        this.parentField = parentField;
        this.defineClassMethod = defineClassMethod;
        this.definePackageMethod = definePackageMethod;
        this.getPackageMethod = getPackageMethod;
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
    Package definePackage(ClassLoader classLoader,
                          String name,
                          String specTitle,
                          String specVersion,
                          String specVendor,
                          String implTitle,
                          String implVersion,
                          String implVendor,
                          URL sealBase) throws IllegalArgumentException {
        Method m = definePackageMethod;
        m.setAccessible(true);
        return (Package) ReflectionUtils.invoke(
                m,
                classLoader,
                name,
                specTitle,
                specVersion,
                specVendor,
                implTitle,
                implVersion,
                implVendor,
                sealBase);
    }

    @Override
    Package getPackage(ClassLoader classLoader, String name) {
        Method m = getPackageMethod;
        m.setAccessible(true);
        return (Package) ReflectionUtils.invoke(m, classLoader, name);
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        Field f = this.parentField;
        f.setAccessible(true);
        ReflectionUtils.set(f, classLoader, parent);
    }

    static ReflectionClassLoaderHelper create(Field parentField) {
        Class<?> c = ClassLoader.class;
        return new ReflectionClassLoaderHelper(
                parentField,
                ReflectionUtils.getDeclaredMethod(
                        c,
                        "defineClass",
                        String.class,
                        byte[].class,
                        int.class,
                        int.class,
                        ProtectionDomain.class),
                ReflectionUtils.getDeclaredMethod(
                        c,
                        "definePackage",
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        URL.class),
                ReflectionUtils.getDeclaredMethod(c, "getPackage", String.class));
    }

}
