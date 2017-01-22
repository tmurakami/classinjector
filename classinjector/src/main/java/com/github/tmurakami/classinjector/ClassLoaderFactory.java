package com.github.tmurakami.classinjector;

final class ClassLoaderFactory {

    static final ClassLoaderFactory INSTANCE = new ClassLoaderFactory();

    private ClassLoaderFactory() {
    }

    ClassLoader newClassLoader(ClassLoader parent,
                               ClassSource source,
                               ClassLoader injectionTarget) {
        return new StealthClassLoader(parent, source, injectionTarget);
    }

}
