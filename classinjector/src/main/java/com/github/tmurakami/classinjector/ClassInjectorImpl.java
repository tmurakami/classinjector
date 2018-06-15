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

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import sun.misc.Unsafe;

final class ClassInjectorImpl extends ClassInjector {

    private static final Field PARENT_FIELD;

    static {
        try {
            PARENT_FIELD = ClassLoader.class.getDeclaredField("parent");
        } catch (NoSuchFieldException e) {
            throw (Error) new NoSuchFieldError(e.getMessage()).initCause(e);
        }
    }

    private final InjectorClassLoaderFactory injectorClassLoaderFactory;

    ClassInjectorImpl(InjectorClassLoaderFactory injectorClassLoaderFactory) {
        this.injectorClassLoaderFactory = injectorClassLoaderFactory;
    }

    @Override
    public void into(@Nonnull ClassLoader target) {
        ClassLoader parent = target.getParent();
        if (target instanceof InjectorClassLoader || parent instanceof InjectorClassLoader) {
            throw new IllegalArgumentException("'target' has already been injected");
        }
        Field parentField = PARENT_FIELD;
        ClassLoader newParent = injectorClassLoaderFactory.newInjectorClassLoader(parent, target);
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
            unsafe.putObject(target, unsafe.objectFieldOffset(parentField), newParent);
            return;
        } catch (Throwable ignored) {
        }
        try {
            while (true) {
                parentField.setAccessible(true);
                try {
                    parentField.set(target, newParent);
                    return;
                } catch (IllegalAccessException ignored) {
                }
            }
        } catch (Throwable t) {
            throw new IllegalStateException("This Java version is not supported");
        }
    }

}
