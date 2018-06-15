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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import com.github.tmurakami.classinjector.ClassFile;
import com.github.tmurakami.classinjector.ClassInjector;
import com.github.tmurakami.classinjector.ClassSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.MyAndroidTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ClassInjectorTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder(context.getCacheDir());

    @SuppressWarnings("deprecation")
    @Test
    public void a_class_should_be_injected_into_a_class_loader() throws Exception {
        String src = context.getApplicationInfo().sourceDir;
        String out = folder.newFile().getCanonicalPath();
        final dalvik.system.DexFile dexFile = dalvik.system.DexFile.loadDex(src, out, 0);
        ClassLoader classLoader = new ClassLoader() {
        };
        ClassInjector.from(new ClassSource() {
            @NonNull
            @Override
            public ClassFile getClassFile(@NonNull String className) {
                return new DexClassFile(className, dexFile);
            }
        }).into(classLoader);
        assertEquals("com.github.tmurakami.classinjector.InjectorClassLoader", classLoader.getParent().getClass().getName());
        String className = MyAndroidTestClass.class.getName();
        Class<?> c = classLoader.loadClass(className);
        assertEquals(className, c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

}
