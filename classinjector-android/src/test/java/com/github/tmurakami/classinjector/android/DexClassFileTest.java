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

import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class DexClassFileTest {

    @Mock
    private dalvik.system.DexFile dexFile;

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_IllegalArgumentException_if_the_className_is_empty() {
        new DexClassFile("", dexFile);
    }

    @Test
    public void toClass_should_return_the_Class_with_the_given_name() {
        Class<?> c = getClass();
        given(dexFile.entries()).willReturn(Collections.enumeration(Collections.singleton("foo.Bar")));
        ClassLoader classLoader = new ClassLoader() {
        };
        given(dexFile.loadClass("foo.Bar", classLoader)).willReturn(c);
        assertSame(c, new DexClassFile("foo.Bar", dexFile).toClass(classLoader));
    }

}
