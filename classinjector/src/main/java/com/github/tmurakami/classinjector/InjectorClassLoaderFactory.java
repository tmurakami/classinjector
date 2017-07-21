package com.github.tmurakami.classinjector;

final class InjectorClassLoaderFactory {
    InjectorClassLoader newInjectorClassLoader(ClassLoader parent,
                                               ClassSource source,
                                               ClassLoader injectionTarget) {
        return new InjectorClassLoader(parent, source, injectionTarget);
    }
}
