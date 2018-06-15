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

import javax.annotation.Nonnull;

/**
 * An object that injects classes into a class loader.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ClassInjector {

    ClassInjector() {
    }

    /**
     * Injects classes into the given class loader.
     *
     * @param target the {@link ClassLoader} with a non-null parent loader
     */
    public abstract void into(@Nonnull ClassLoader target);

    /**
     * Instantiates a new instance.
     *
     * @param source the source of data that make up classes
     * @return the class injector for injecting classes constructed with the specified source into
     * a class loader
     */
    @Nonnull
    public static ClassInjector from(@Nonnull ClassSource source) {
        return new ClassInjectorImpl(new InjectorClassLoaderFactory(source));
    }

}
