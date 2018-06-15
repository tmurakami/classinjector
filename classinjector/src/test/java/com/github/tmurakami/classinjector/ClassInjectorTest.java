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

package com.github.tmurakami.classinjector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.junit.Test;
import test.MyTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ClassInjectorTest {
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    @Test
    public void a_class_should_be_injected_into_a_class_loader() throws Exception {
        ClassLoader classLoader = new ClassLoader(null) {
        };
        ClassInjector.from(new ClassSource() {
            @Nonnull
            @Override
            public ClassFile getClassFile(@Nonnull String className) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = getClass().getResourceAsStream('/' + className.replace('.', '/') + ".class");
                try {
                    byte[] buffer = new byte[16384];
                    for (int l; (l = in.read(buffer)) != -1; ) {
                        out.write(buffer, 0, l);
                    }
                } finally {
                    in.close();
                }
                return new JvmClassFile(className, out.toByteArray());
            }
        }).into(classLoader);
        assertTrue(classLoader.getParent() instanceof InjectorClassLoader);
        String className = MyTestClass.class.getName();
        Class<?> c = classLoader.loadClass(className);
        assertEquals(className, c.getName());
        assertSame(classLoader, c.getClassLoader());
    }
}
