package com.github.tmurakami.classinjector;

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
    public abstract void into(ClassLoader target);

    /**
     * Instantiates a new instance.
     *
     * @param source the source of data that make up classes
     * @return the class injector for injecting classes constructed with the specified source into
     * a class loader
     */
    public static ClassInjector from(ClassSource source) {
        if (source == null) {
            throw new IllegalArgumentException("'source' is null");
        }
        return new ClassInjectorImpl(source, ClassLoaderFactory.INSTANCE, ClassLoaderHelper.INSTANCE);
    }

}
