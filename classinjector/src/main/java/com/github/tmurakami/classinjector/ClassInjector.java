package com.github.tmurakami.classinjector;

import javax.annotation.Nonnull;

/**
 * The class injector is an object that injects classes into a class loader.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ClassInjector {

    ClassInjector() {
    }

    /**
     * Inject classes into the given class loader.
     *
     * @param target the {@link ClassLoader} with a non-null parent loader
     */
    public abstract void into(@Nonnull ClassLoader target);

    /**
     * Instantiates a new instance.
     *
     * @param source the source of data that make up classes
     * @return the class injector for injecting classes constructed with the specified source into
     * a class loader
     */
    @Nonnull
    public static ClassInjector from(@Nonnull ClassSource source) {
        return new ClassInjectorImpl(source, ClassLoaderFactory.INSTANCE, ClassLoaderHelper.INSTANCE);
    }

}
