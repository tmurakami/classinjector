package com.github.tmurakami.classinjector;

import java.security.AccessController;
import java.security.PrivilegedAction;

final class ClassLoaderFactory {

    static final ClassLoaderFactory INSTANCE = new ClassLoaderFactory();

    private ClassLoaderFactory() {
    }

    ClassLoader newClassLoader(final ClassLoader parent,
                               final ClassSource source,
                               final ClassLoader injectionTarget) {
        if (System.getSecurityManager() == null) {
            return new StealthClassLoader(parent, source, injectionTarget);
        } else {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return new StealthClassLoader(parent, source, injectionTarget);
                }
            });
        }
    }

}
