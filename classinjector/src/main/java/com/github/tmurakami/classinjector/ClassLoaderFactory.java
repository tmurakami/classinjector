package com.github.tmurakami.classinjector;

final class ClassLoaderFactory {

    static final ClassLoaderFactory INSTANCE = new ClassLoaderFactory();

    private ClassLoaderFactory() {
    }

    ClassLoader newClassLoader(final ClassLoader parent,
                               final ClassSource source,
                               final ClassLoader injectionTarget) {
        return new StealthClassLoader(parent, source, injectionTarget);
    }

}
