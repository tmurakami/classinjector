package com.github.tmurakami.classinjector;

final class ClassLoaderFactory {

    static final ClassLoaderFactory INSTANCE = new ClassLoaderFactory();

    private ClassLoaderFactory() {
    }

    ClassLoader newClassLoader(ClassSource source, ClassLoader injectionTarget) {
        return new StealthClassLoader(source, injectionTarget);
    }

}
