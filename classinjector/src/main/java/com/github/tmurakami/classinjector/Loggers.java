package com.github.tmurakami.classinjector;

import java.util.logging.Logger;

final class Loggers {

    private static final String NAME;

    static {
        String className = Loggers.class.getName();
        NAME = className.substring(0, className.lastIndexOf('.'));
    }

    private Loggers() {
        throw new AssertionError("Do not instantiate");
    }

    static Logger get() {
        return Logger.getLogger(NAME);
    }

}
