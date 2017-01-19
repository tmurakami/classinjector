package com.github.tmurakami.classinjector.android;

import com.github.tmurakami.classinjector.ClassFile;

import dalvik.system.DexFile;

/**
 * A {@link ClassFile} for Android Dalvik/ART.
 */
public final class DexClassFile implements ClassFile {

    private final String className;
    private final DexFile dexFile;

    /**
     * Create an instance.
     *
     * @param className The class name
     * @param dexFile   The dex file including the data for the given class name
     */
    public DexClassFile(String className, DexFile dexFile) {
        this.className = className;
        this.dexFile = dexFile;
    }

    @Override
    public Class toClass(ClassLoader classLoader) {
        return dexFile.loadClass(className, classLoader);
    }

}
