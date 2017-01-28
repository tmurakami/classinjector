package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

final class UnsafeWrapper {

    private final Object unsafe;

    UnsafeWrapper(Object unsafe) {
        this.unsafe = unsafe;
    }

    Class defineClass(String name,
                      byte[] b,
                      int off,
                      int len,
                      ClassLoader loader,
                      ProtectionDomain protectionDomain) {
        return getUnsafe().defineClass(name, b, off, len, loader, protectionDomain);
    }

    long objectFieldOffset(Field f) {
        return getUnsafe().objectFieldOffset(f);
    }

    void putObject(Object o, long offset, Object x) {
        getUnsafe().putObject(o, offset, x);
    }

    private sun.misc.Unsafe getUnsafe() {
        return (sun.misc.Unsafe) unsafe;
    }

}
