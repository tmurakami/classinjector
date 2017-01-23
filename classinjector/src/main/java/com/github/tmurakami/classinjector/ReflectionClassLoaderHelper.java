package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;

final class ReflectionClassLoaderHelper extends ClassLoaderHelper {

    private Field parentField;
    private Method defineClassMethod;
    private Method definePackageMethod;
    private Method getPackageMethod;

    private ReflectionClassLoaderHelper() {
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
        return (Class) invoke(m, classLoader, name, b, off, len, protectionDomain);
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
        return (Package) invoke(
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
        return (Package) invoke(m, classLoader, name);
    }

    @Override
    void setParent(ClassLoader classLoader, ClassLoader parent) {
        Field f = parentField;
        f.setAccessible(true);
        try {
            f.set(classLoader, parent);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    static ReflectionClassLoaderHelper create(Field parentField) throws NoSuchMethodException {
        Class<?> c = ClassLoader.class;
        ReflectionClassLoaderHelper o = new ReflectionClassLoaderHelper();
        o.parentField = parentField;
        o.defineClassMethod = c.getDeclaredMethod(
                "defineClass",
                String.class,
                byte[].class,
                int.class,
                int.class,
                ProtectionDomain.class);
        o.definePackageMethod = c.getDeclaredMethod(
                "definePackage",
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                URL.class);
        o.getPackageMethod = c.getDeclaredMethod("getPackage", String.class);
        return o;
    }

    private static Object invoke(Method method, Object o, Object... parameters) {
        try {
            return method.invoke(o, parameters);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new IllegalStateException("Cannot invoke "
                        + method.getDeclaringClass().getName() + '#' + method.getName(), cause);
            }
        }
    }

}
