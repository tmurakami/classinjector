package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

final class UnsafeWrapper {

    private final sun.misc.Unsafe unsafe;

    UnsafeWrapper(Object unsafe) {
        this.unsafe = (sun.misc.Unsafe) unsafe;
    }

    Class defineClass(String name,
                      byte[] b,
                      int off,
                      int len,
                      ClassLoader loader,
                      ProtectionDomain protectionDomain) {
        return unsafe.defineClass(name, b, off, len, loader, protectionDomain);
    }

    long objectFieldOffset(Field f) {
        return unsafe.objectFieldOffset(f);
    }

    void putObject(Object o, long offset, Object x) {
        unsafe.putObject(o, offset, x);
    }

}
