package com.github.tmurakami.classinjector.android;

import android.support.annotation.NonNull;

import com.github.tmurakami.classinjector.ClassFile;

import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * The {@link ClassFile} for Android VM.
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
    public DexClassFile(@NonNull String className, @NonNull DexFile dexFile) {
        if (className.isEmpty()) {
            throw new IllegalArgumentException("'className' is empty");
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

    @NonNull
    @Override
    public Class toClass(@NonNull ClassLoader classLoader) {
        return dexFile.loadClass(className, classLoader);
    }

}
