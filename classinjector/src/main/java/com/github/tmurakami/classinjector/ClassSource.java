package com.github.tmurakami.classinjector;

import java.io.IOException;

/**
 * An object that obtains {@link ClassFile} objects.
 */
public interface ClassSource {

    /**
     * Return the {@link ClassFile} for the given name.
     *
     * @param className The class name
     * @return The {@link ClassFile} object, or null if not found.
     * @throws IOException If an IO error occurred.
     */
    ClassFile getClassFile(String className) throws IOException;

}
