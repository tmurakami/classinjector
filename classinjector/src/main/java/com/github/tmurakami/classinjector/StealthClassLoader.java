package com.github.tmurakami.classinjector;

import java.io.IOError;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

final class StealthClassLoader extends ClassLoader {

    private static final String MY_PACKAGE = "com.github.tmurakami.classinjector.";

    private final ClassSource source;
    private final ClassLoader injectionTarget;
    private final Set<String> findingClasses = new HashSet<>();

    @SuppressWarnings("WeakerAccess")
    StealthClassLoader(ClassLoader parent, ClassSource source, ClassLoader injectionTarget) {
        super(parent);
        this.source = source;
        this.injectionTarget = injectionTarget;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Set<String> classes = findingClasses;
        if (classes.contains(name)) {
            throw new IllegalStateException(name + " is recursively loaded");
        }
        if (!name.startsWith(MY_PACKAGE)) {
            ClassFile f;
            try {
                f = source.getClassFile(name);
            } catch (IOException e) {
                throw new IOError(e);
            }
            if (f != null) {
                Class<?> c;
                classes.add(name);
                try {
                    c = f.toClass(injectionTarget);
                } finally {
                    classes.remove(name);
                }
                if (c == null) {
                    throw new NoClassDefFoundError("No class created from the class file for " + name);
                }
                return c;
            }
        }
        return super.findClass(name);
    }

}
