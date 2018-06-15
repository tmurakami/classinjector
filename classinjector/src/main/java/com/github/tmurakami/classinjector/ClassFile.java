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
 * An object representing data making up a class.
 */
public interface ClassFile {

    /**
     * Instantiates a new {@link Class} instance.
     *
     * @param classLoader the class loader to use for creating a class
     * @return the class object that was created from this file
     */
    @Nonnull
    Class toClass(@Nonnull ClassLoader classLoader);

}
