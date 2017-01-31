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
        if (!name.startsWith(MY_PACKAGE)) {
            Set<String> classes = findingClasses;
            if (classes.contains(name)) {
                throw new IllegalStateException(name + " is recursively loaded");
            }
            ClassFile f;
            try {
                f = source.getClassFile(name);
            } catch (IOException e) {
                throw new IOError(e);
            }
            if (f != null) {
                classes.add(name);
                try {
                    return f.toClass(injectionTarget);
                } finally {
                    classes.remove(name);
                }
            }
        }
        return super.findClass(name);
    }

}
