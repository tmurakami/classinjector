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
        for (ClassLoader l = target; l != null; l = l.getParent()) {
            if (l instanceof StealthClassLoader) {
                throw new IllegalArgumentException(ClassSource.class.getSimpleName()
                        + " has already been injected into " + target);
            }
        }
        classLoaderHelper.setParent(target, classLoaderFactory.newClassLoader(source, target));
    }

}
