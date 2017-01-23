package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

final class Unsafe {

    private final sun.misc.Unsafe delegate;

    private Unsafe(sun.misc.Unsafe delegate) {
        this.delegate = delegate;
    }

    Class defineClass(String name,
                      byte[] b,
                      int off,
                      int len,
                      ClassLoader loader,
                      ProtectionDomain protectionDomain) {
        return delegate.defineClass(name, b, off, len, loader, protectionDomain);
    }

    long objectFieldOffset(Field f) {
        return delegate.objectFieldOffset(f);
    }

    void putObject(Object o, long offset, Object x) {
        delegate.putObject(o, offset, x);
    }

    static Unsafe wrap(Object sunMiscUnsafe) {
        return new Unsafe((sun.misc.Unsafe) sunMiscUnsafe);
    }

}
