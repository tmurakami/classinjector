package com.github.tmurakami.classinjector;

/**
 * An object representing data making up a class.
 */
public interface ClassFile {

    /**
     * Create an instance of {@link Class} with the given class loader.
     *
     * @param classLoader The {@link ClassLoader} to use for creating a class
     * @return The {@link Class} object that was created from this file.
     */
    Class toClass(ClassLoader classLoader);

}
