package com.github.tmurakami.classinjector;

/**
 * The class file is an object representing data making up a class.
 */
public interface ClassFile {

    /**
     * Instantiates a new {@link Class} instance.
     *
     * @param classLoader the class loader to use for creating a class
     * @return the class object that was created from this file
     */
    Class toClass(ClassLoader classLoader);

}
