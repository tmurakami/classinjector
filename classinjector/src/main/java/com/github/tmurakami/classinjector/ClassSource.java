package com.github.tmurakami.classinjector;

import java.io.IOException;

/**
 * The Class source is an object that obtains {@link ClassFile} objects.
 */
public interface ClassSource {

    /**
     * Gets class file for the given name.
     *
     * @param className the class name
     * @return the class file, or null if not found
     * @throws IOException if an IO error occurred
     */
    ClassFile getClassFile(String className) throws IOException;

}
