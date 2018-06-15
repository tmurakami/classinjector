# ClassInjector

[![CircleCI](https://circleci.com/gh/tmurakami/classinjector.svg?style=shield)](https://circleci.com/gh/tmurakami/classinjector)
[![Release](https://jitpack.io/v/tmurakami/classinjector.svg)](https://jitpack.io/#tmurakami/classinjector)<br>
![Java](https://img.shields.io/badge/Java-7%2B-blue.svg)
![Android](https://img.shields.io/badge/Android-4.0%2B-blue.svg)

A library that provides the ability to inject classes into a class loader.

## Usage

```java
ClassInjector.from(new ClassSource() {
    @Nullable
    @Override
    public ClassFile getClassFile(@Nonnull String className) throws IOException {
        // Find bytecode for the given class name from arbitrary sources.
        byte[] bytecode = ...
        // Return the ClassFile object with the given class name and its bytecode.
        // If no bytecode was found, return null.
        return bytecode == null ? null : new JvmClassFile(className, bytecode);
    }
}).into(getClass().getClassLoader());
```

For Android, use DexClassFile instead of JvmClassFile.

```java
// Get a Context object.
final Context context = ...
ClassInjector.from(new ClassSource() {
    @Nullable
    @Override
    public ClassFile getClassFile(@NonNull String className) throws IOException {
        // Find a zip file containing "classes.dex" with the given class name.
        String sourcePath = ...
        // If not found, return null.
        if (sourcePath == null) {
            return null;
        }
        // Construct the path for the optimized dex data.
        File cacheDir = context.getDir("dex_cache", Context.MODE_PRIVATE);
        String optimizedPath = new File(cacheDir, "classes.dex").getCanonicalPath();
        // Open the dex file.
        DexFile dexFile = DexFile.loadDex(sourcePath, optimizedPath, 0);
        // Return the ClassFile object with the given class name and its dex file.
        return new DexClassFile(className, dexFile);
    }
}).into(context.getClassLoader());
```

## Installation

First, add the [JitPack](https://jitpack.io/) repository to your build.gradle.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

And then, for Java (7 or later):

```groovy
dependencies {
    compile 'com.github.tmurakami.classinjector:classinjector:1.0.0'
}
```

Or for Android (2.3 or later):

```groovy
android {
    defaultConfig {
        minSdkVersion 9 // 9 or higher
    }
}

dependencies {
    compile 'com.github.tmurakami.classinjector:classinjector-android:1.0.0'
}
```

## License

```
Copyright 2017 Tsuyoshi Murakami

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
