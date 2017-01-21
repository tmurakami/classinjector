package com.github.tmurakami.classinjector.android;

import com.github.tmurakami.classinjector.ClassFile;

import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * This class file is for Android Dalvik/ART.
 */
@SuppressWarnings("WeakerAccess")
public final class DexClassFile implements ClassFile {

    private final String className;
    private final DexFile dexFile;

    /**
     * Instantiates a new instance.
     *
     * @param className the class name
     * @param dexFile   the dex file including the data for the given class name
     */
    public DexClassFile(String className, DexFile dexFile) {
        if (className == null) {
            throw new IllegalArgumentException("'className' is null");
        }
        if (className.isEmpty()) {
            throw new IllegalArgumentException("'className' is empty string");
        }
        if (dexFile == null) {
            throw new IllegalArgumentException("'dexFile' is null");
        }
        for (Enumeration<String> e = dexFile.entries(); e.hasMoreElements(); ) {
            if (className.equals(e.nextElement())) {
                this.className = className;
                this.dexFile = dexFile;
                return;
            }
        }
        throw new IllegalArgumentException("'dexFile' does not contain data making up class " + className);
    }

    @Override
    public Class toClass(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("'classLoader' is null");
        }
        return dexFile.loadClass(className, classLoader);
    }

}
