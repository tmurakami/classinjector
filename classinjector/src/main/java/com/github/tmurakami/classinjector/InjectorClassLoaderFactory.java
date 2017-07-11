package com.github.tmurakami.classinjector;

final class InjectorClassLoaderFactory {

    static final InjectorClassLoaderFactory INSTANCE = new InjectorClassLoaderFactory();

    private InjectorClassLoaderFactory() {
    }

    InjectorClassLoader newInjectorClassLoader(ClassLoader parent,
                                               ClassSource source,
                                               ClassLoader injectionTarget) {
        return new InjectorClassLoader(parent, source, injectionTarget);
    }

}
