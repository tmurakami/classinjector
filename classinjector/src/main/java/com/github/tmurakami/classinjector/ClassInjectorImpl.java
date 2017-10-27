package com.github.tmurakami.classinjector;

import javax.annotation.Nonnull;

final class ClassInjectorImpl extends ClassInjector {

    private final ClassSource source;
    private final ClassLoaderHelper classLoaderHelper;
    private final InjectorClassLoaderFactory injectorClassLoaderFactory;

    ClassInjectorImpl(ClassSource source,
                      ClassLoaderHelper classLoaderHelper,
                      InjectorClassLoaderFactory injectorClassLoaderFactory) {
        this.source = source;
        this.classLoaderHelper = classLoaderHelper;
        this.injectorClassLoaderFactory = injectorClassLoaderFactory;
    }

    @Override
    public void into(@Nonnull ClassLoader target) {
        ClassLoaderHelper h = classLoaderHelper;
        ClassLoader parent = h.getParent(target);
        if (parent == null) {
            throw new IllegalArgumentException("The parent of 'target' is null");
        }
        if (target instanceof InjectorClassLoader || parent instanceof InjectorClassLoader) {
            throw new IllegalArgumentException("'target' has already been injected");
        }
        h.setParent(target, injectorClassLoaderFactory.newInjectorClassLoader(parent, source, target));
    }

}
