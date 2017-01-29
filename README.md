# ClassInjector

[![CircleCI](https://circleci.com/gh/tmurakami/classinjector.svg?style=shield)](https://circleci.com/gh/tmurakami/classinjector)
[![Release](https://jitpack.io/v/tmurakami/classinjector.svg)](https://jitpack.io/#tmurakami/classinjector)  
![Java](https://img.shields.io/badge/Java-7%2B-blue.svg)
![Android](https://img.shields.io/badge/Android-2.3%2B-blue.svg)

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
    compile 'com.github.tmurakami.classinjector:classinjector:x.y.z'
    compileOnly 'com.google.code.findbugs:jsr305:3.0.1'
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
    compile 'com.github.tmurakami.classinjector:classinjector-android:x.y.z'
    provided 'com.android.support:support-annotations:x.y.z'
}
```
