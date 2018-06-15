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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An object representing a group of {@link ClassSource} objects.
 */
@SuppressWarnings("WeakerAccess")
public final class ClassSources implements ClassSource {

    private final List<ClassSource> sources;

    /**
     * Instantiates a new instance.
     *
     * @param sources the collection of {@link ClassSource} objects
     */
    public ClassSources(@Nonnull Iterable<? extends ClassSource> sources) {
        List<ClassSource> list = new ArrayList<>();
        for (ClassSource s : sources) {
            if (s == null) {
                throw new IllegalArgumentException("'sources' contains null");
            }
            list.add(s);
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("'sources' is empty");
        }
        this.sources = list;
    }

    @Nullable
    @Override
    public ClassFile getClassFile(@Nonnull String className) throws IOException {
        if (className.isEmpty()) {
            throw new IllegalArgumentException("'className' is empty");
        }
        for (ClassSource b : sources) {
            ClassFile f = b.getClassFile(className);
            if (f != null) {
                return f;
            }
        }
        return null;
    }

}
