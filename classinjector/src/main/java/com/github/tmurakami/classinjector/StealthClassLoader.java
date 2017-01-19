package com.github.tmurakami.classinjector;

import java.io.IOError;
import java.io.IOException;

final class StealthClassLoader extends ClassLoader {

    private static final String MY_PACKAGE = "com.github.tmurakami.classinjector.";

    private final ClassSource source;
    private final ClassLoader injectionTarget;

    @SuppressWarnings("WeakerAccess")
    StealthClassLoader(ClassSource source, ClassLoader injectionTarget) {
        super(injectionTarget.getParent());
        this.source = source;
        this.injectionTarget = injectionTarget;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (!name.startsWith(MY_PACKAGE)) {
            try {
                ClassFile f = source.getClassFile(name);
                if (f != null) {
                    Class<?> c = f.toClass(injectionTarget);
                    if (c == null) {
                        throw new NoClassDefFoundError("No class created from the class file for " + name);
                    }
                    return c;
                }
            } catch (IOException e) {
                throw new IOError(e);
            }
        }
        return super.findClass(name);
    }

}
