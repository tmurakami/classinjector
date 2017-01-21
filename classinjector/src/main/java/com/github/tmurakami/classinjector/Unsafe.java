package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

final class Unsafe {

    private static final Unsafe UNSAFE = newUnsafe();

    private Object unsafe;
    private Method defineClassMethod;
    private Method objectFieldOffsetMethod;
    private Method putObjectMethod;
    private ReflectionHelper reflectionHelper;

    private Unsafe() {
    }

    Class defineClass(String name,
                      byte[] b,
                      int off,
                      int len,
                      ClassLoader loader,
                      ProtectionDomain protectionDomain) {
        return (Class) reflectionHelper.invoke(
                defineClassMethod, unsafe, name, b, off, len, loader, protectionDomain);
    }

    long objectFieldOffset(Field f) {
        return (Long) reflectionHelper.invoke(objectFieldOffsetMethod, unsafe, f);
    }

    void putObject(Object o, long offset, Object x) {
        reflectionHelper.invoke(putObjectMethod, unsafe, o, offset, x);
    }

    static Unsafe getUnsafe() {
        return UNSAFE;
    }

    private static Unsafe newUnsafe() {
        String name = "sun.misc.Unsafe";
        Class<?> c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(name);
        }
        Unsafe o = new Unsafe();
        ReflectionHelper h = ReflectionHelper.INSTANCE;
        Field f = h.getDeclaredField(c, "theUnsafe");
        h.setAccessible(f, true);
        o.unsafe = h.get(f, null);
        o.defineClassMethod = h.getMethod(
                c,
                "defineClass",
                String.class,
                byte[].class,
                int.class,
                int.class,
                ClassLoader.class,
                ProtectionDomain.class);
        o.objectFieldOffsetMethod = h.getMethod(c, "objectFieldOffset", Field.class);
        o.putObjectMethod = h.getMethod(
                c, "putObject", Object.class, long.class, Object.class);
        o.reflectionHelper = h;
        return o;
    }

}
