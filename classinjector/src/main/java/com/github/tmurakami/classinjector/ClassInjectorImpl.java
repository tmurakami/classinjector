package com.github.tmurakami.classinjector;

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
    public void into(ClassLoader target) {
        if (target == null) {
            throw new IllegalArgumentException("'target' is null");
        }
        ClassLoader parent = target.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("The parent of 'target' is null");
        }
        boolean alreadyInjected = target instanceof StealthClassLoader;
        for (ClassLoader l = parent; !alreadyInjected && l != null; l = l.getParent()) {
            if (l instanceof StealthClassLoader) {
                alreadyInjected = true;
            }
        }
        if (alreadyInjected) {
            throw new IllegalArgumentException(target + " has already been injected");
        }
        classLoaderHelper.setParent(target, classLoaderFactory.newClassLoader(parent, source, target));
    }

}
