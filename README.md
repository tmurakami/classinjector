# ClassInjector

[![CircleCI](https://circleci.com/gh/tmurakami/classinjector.svg?style=shield)](https://circleci.com/gh/tmurakami/classinjector)
[![Release](https://jitpack.io/v/tmurakami/classinjector.svg)](https://jitpack.io/#tmurakami/classinjector)

A library that provides the ability to inject classes into a class loader at runtime.

## Example

```java
ClassLoader injectionTarget = ...
ClassInjector.from(new ClassSource() {
    @Override
    public ClassFile getClassFile(String className) throws IOException {
        // Find bytecode from arbitrary sources.
        byte[] bytecode = ...
        // Return the ClassFile object with the given class name and its bytecode.
        // If no bytecode was found, return null.
        return bytecode == null ? null : new JvmClassFile(className, bytecode);
    }
}).into(injectionTarget);
```

For Android, use DexClassFile instead of JvmClassFile.
```java
ClassLoader injectionTarget = ...
ClassInjector.from(new ClassSource() {
    @Override
    public ClassFile getClassFile(String className) throws IOException {
        // Find a zip file containing "classes.dex" with the given class name.
        String sourcePath = ...
        // If not found, return null.
        if (sourcePath == null) {
            return null;
        }
        // Get a Context object.
        Context context = ...
        // Construct the path for the optimized dex data.
        File cacheDir = context.getDir("dex_cache", Context.MODE_PRIVATE);
        String optimizedPath = new File(cacheDir, "classes.dex").getCanonicalPath();
        // Open a dex file.
        DexFile dexFile = DexFile.loadDex(sourcePath, optimizedPath, 0);
        // Return the ClassFile object with the given class name and its dex file.
        return new DexClassFile(className, dexFile);
    }
}).into(injectionTarget);
```

## Installation

First, add the [JitPack](https://jitpack.io/) repository to your build.gradle.
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

And then, for Java (6 or later):
```groovy
dependencies {
    compile 'com.github.tmurakami.classinjector:classinjector:x.y.z'
}
```

Or for Android (2.3 or later):
```groovy
android {
    defaultConfig {
        minSdkVersion 9 // Require 9 or higher.
    }
}

dependencies {
    compile 'com.github.tmurakami.classinjector:classinjector-android:x.y.z'
}
```
