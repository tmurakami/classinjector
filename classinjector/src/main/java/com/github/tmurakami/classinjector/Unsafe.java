package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

final class Unsafe {

    private static final Unsafe UNSAFE = newUnsafe();

    private final Object unsafe;
    private final Method defineClassMethod;
    private final Method objectFieldOffsetMethod;
    private final Method putObjectMethod;

    private Unsafe(Object unsafe,
                   Method defineClassMethod,
                   Method objectFieldOffsetMethod,
                   Method putObjectMethod) {
        this.unsafe = unsafe;
        this.defineClassMethod = defineClassMethod;
        this.objectFieldOffsetMethod = objectFieldOffsetMethod;
        this.putObjectMethod = putObjectMethod;
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
        Field f = ReflectionUtils.getDeclaredField(c, "theUnsafe");
        f.setAccessible(true);
        return new Unsafe(
                ReflectionUtils.get(f, null),
                ReflectionUtils.getMethod(
                        c,
                        "defineClass",
                        String.class,
                        byte[].class,
                        int.class,
                        int.class,
                        ClassLoader.class,
                        ProtectionDomain.class),
                ReflectionUtils.getMethod(c, "objectFieldOffset", Field.class),
                ReflectionUtils.getMethod(c, "putObject", Object.class, long.class, Object.class));
    }

}
