package com.github.tmurakami.classinjector;

/**
 * This class file is for Java VM.
 */
@SuppressWarnings("WeakerAccess")
public final class JvmClassFile implements ClassFile {

    private final String className;
    private final byte[] bytecode;
    private final ClassLoaderHelper classLoaderHelper;

    /**
     * Instantiates a new instance.
     *
     * @param className the class name
     * @param bytecode  the byte array that make up the class
     */
    public JvmClassFile(String className, byte[] bytecode) {
        this(className, bytecode, ClassLoaderHelper.INSTANCE);
    }

    JvmClassFile(String className, byte[] bytecode, ClassLoaderHelper classLoaderHelper) {
        if (className == null) {
            throw new IllegalArgumentException("'className' is null");
        }
        if (className.isEmpty()) {
            throw new IllegalArgumentException("'className' is empty string");
        }
        if (bytecode == null) {
            throw new IllegalArgumentException("'bytecode' is null");
        }
        if (bytecode.length == 0) {
            throw new IllegalArgumentException("'bytecode' is empty byte array");
        }
        this.className = className;
        this.classLoaderHelper = classLoaderHelper;
        int length = bytecode.length;
        byte[] bytes = new byte[length];
        System.arraycopy(bytecode, 0, bytes, 0, length);
        this.bytecode = bytes;
    }

    @Override
    public Class toClass(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("'classLoader' is null");
        }
        String name = className;
        int dot = name.lastIndexOf('.');
        if (dot > -1) {
            String pkg = name.substring(0, dot);
            if (classLoaderHelper.getPackage(classLoader, pkg) == null) {
                try {
                    classLoaderHelper.definePackage(classLoader, pkg, null, null, null, null, null, null, null);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        byte[] b = bytecode;
        return classLoaderHelper.defineClass(classLoader, name, b, 0, b.length, null);
    }

}
