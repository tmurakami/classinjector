package com.github.tmurakami.classinjector;

import javax.annotation.Nonnull;

/**
 * An object that injects classes into a class loader.
 */
@SuppressWarnings("WeakerAccess")
public final class ClassInjector {

    private final ClassSource source;
    private final ClassLoaderHelper classLoaderHelper;
    private final InjectorClassLoaderFactory injectorClassLoaderFactory;

    ClassInjector(ClassSource source,
                  ClassLoaderHelper classLoaderHelper,
                  InjectorClassLoaderFactory injectorClassLoaderFactory) {
        this.source = source;
        this.classLoaderHelper = classLoaderHelper;
        this.injectorClassLoaderFactory = injectorClassLoaderFactory;
    }

    /**
     * Injects classes into the given class loader.
     *
     * @param target the {@link ClassLoader} with a non-null parent loader
     */
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

    /**
     * Instantiates a new instance.
     *
     * @param source the source of data that make up classes
     * @return the class injector for injecting classes constructed with the specified source into
     * a class loader
     */
    @Nonnull
    public static ClassInjector from(@Nonnull ClassSource source) {
        return new ClassInjector(source, ClassLoaderHelper.INSTANCE, new InjectorClassLoaderFactory());
    }

}
