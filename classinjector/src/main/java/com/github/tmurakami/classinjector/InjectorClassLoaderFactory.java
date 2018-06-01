package com.github.tmurakami.classinjector;

final class InjectorClassLoaderFactory {

    private final ClassSource source;

    InjectorClassLoaderFactory(ClassSource source) {
        this.source = source;
    }

    ClassLoader newInjectorClassLoader(ClassLoader parent, ClassLoader injectionTarget) {
        return new InjectorClassLoader(parent, source, injectionTarget);
    }

}
