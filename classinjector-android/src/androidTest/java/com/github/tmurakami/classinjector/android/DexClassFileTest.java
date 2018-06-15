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
import android.support.test.InstrumentationRegistry;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.MyAndroidTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class DexClassFileTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder(context.getCacheDir());

    @SuppressWarnings("deprecation")
    @Test
    public void toClass_should_return_the_Class_with_the_given_name() throws Exception {
        String src = context.getApplicationInfo().sourceDir;
        String out = folder.newFile().getCanonicalPath();
        dalvik.system.DexFile dexFile = dalvik.system.DexFile.loadDex(src, out, 0);
        ClassLoader classLoader = new ClassLoader() {
        };
        String className = MyAndroidTestClass.class.getName();
        Class<?> c = new DexClassFile(className, dexFile).toClass(classLoader);
        assertEquals(className, c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

}
