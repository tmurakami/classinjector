package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;

final class ReflectionClassLoaderHelper extends ClassLoaderHelper {

    private Field parentField;
    private Method defineClassMethod;
    private Method definePackageMethod;
    private Method getPackageMethod;
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
        return (Class) h.invoke(m, classLoader, name, b, off, len, protectionDomain);
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
        ReflectionHelper h = reflectionHelper;
        Method m = definePackageMethod;
        h.setAccessible(m, true);
        return (Package) h.invoke(m,
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
        ReflectionHelper h = reflectionHelper;
        Method m = getPackageMethod;
        h.setAccessible(m, true);
        return (Package) h.invoke(m, classLoader, name);
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        ReflectionHelper helper = reflectionHelper;
        Field f = parentField;
        helper.setAccessible(f, true);
        helper.set(f, classLoader, parent);
    }

    static ReflectionClassLoaderHelper create(Field parentField) {
        Class<?> c = ClassLoader.class;
        ReflectionClassLoaderHelper o = new ReflectionClassLoaderHelper();
        o.parentField = parentField;
        ReflectionHelper h = ReflectionHelper.INSTANCE;
        o.defineClassMethod = h.getDeclaredMethod(
                c,
                "defineClass",
                String.class,
                byte[].class,
                int.class,
                int.class,
                ProtectionDomain.class);
        o.definePackageMethod = h.getDeclaredMethod(
                c,
                "definePackage",
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                URL.class);
        o.getPackageMethod = h.getDeclaredMethod(c, "getPackage", String.class);
        o.reflectionHelper = h;
        return o;
    }

}
