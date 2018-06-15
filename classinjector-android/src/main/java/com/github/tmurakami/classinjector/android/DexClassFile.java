/*
 * Copyright 2017 Tsuyoshi Murakami
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tmurakami.classinjector.android;

import java.util.Enumeration;
import javax.annotation.Nonnull;
import com.github.tmurakami.classinjector.ClassFile;

/**
 * The {@link ClassFile} for Android VM.
 */
@SuppressWarnings({"WeakerAccess", "deprecation"})
public final class DexClassFile implements ClassFile {

    private final String className;
    private final dalvik.system.DexFile dexFile;

    /**
     * Instantiates a new instance.
     *
     * @param className the class name
     * @param dexFile   the dex file including the data for the given class name
     */
    public DexClassFile(@Nonnull String className, @Nonnull dalvik.system.DexFile dexFile) {
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

    @Nonnull
    @Override
    public Class toClass(@Nonnull ClassLoader classLoader) {
        return dexFile.loadClass(className, classLoader);
    }

}
