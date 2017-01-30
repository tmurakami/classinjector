package com.github.tmurakami.classinjector;

import javax.annotation.Nonnull;

final class ClassInjectorImpl extends ClassInjector {

    private final ClassSource source;
    private final ClassLoaderFactory classLoaderFactory;
    private final ClassLoaderHelper classLoaderHelper;

    ClassInjectorImpl(ClassSource source,
                      ClassLoaderFactory classLoaderFactory,
                      ClassLoaderHelper classLoaderHelper) {
        this.source = source;
        this.classLoaderFactory = classLoaderFactory;
        this.classLoaderHelper = classLoaderHelper;
    }

    @Override
    public void into(@Nonnull ClassLoader target) {
        ClassLoaderHelper h = classLoaderHelper;
        ClassLoader parent = h.getParent(target);
        if (parent == null) {
            throw new IllegalArgumentException("The parent of 'target' is null");
        }
        if (target instanceof StealthClassLoader || parent instanceof StealthClassLoader) {
            throw new IllegalArgumentException("'target' has already been injected");
        }
        h.setParent(target, classLoaderFactory.newClassLoader(parent, source, target));
    }

}
