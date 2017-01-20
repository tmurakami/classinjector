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

    private Unsafe() {
    }

    Class defineClass(String name,
                      byte[] b,
                      int off,
                      int len,
                      ClassLoader loader,
                      ProtectionDomain protectionDomain) {
        return (Class) ReflectionUtils.invoke(
                defineClassMethod, unsafe, name, b, off, len, loader, protectionDomain);
    }

    long objectFieldOffset(Field f) {
        return (Long) ReflectionUtils.invoke(objectFieldOffsetMethod, unsafe, f);
    }

    void putObject(Object o, long offset, Object x) {
        ReflectionUtils.invoke(putObjectMethod, unsafe, o, offset, x);
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
        Field f = ReflectionUtils.getDeclaredField(c, "theUnsafe");
        f.setAccessible(true);
        o.unsafe = ReflectionUtils.get(f, null);
        o.defineClassMethod = ReflectionUtils.getMethod(
                c,
                "defineClass",
                String.class,
                byte[].class,
                int.class,
                int.class,
                ClassLoader.class,
                ProtectionDomain.class);
        o.objectFieldOffsetMethod = ReflectionUtils.getMethod(c, "objectFieldOffset", Field.class);
        o.putObjectMethod = ReflectionUtils.getMethod(c, "putObject", Object.class, long.class, Object.class);
        return o;
    }

}
