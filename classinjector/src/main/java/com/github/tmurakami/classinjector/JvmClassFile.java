package com.github.tmurakami.classinjector;

/**
 * A {@link ClassFile} for Java VM.
 */
public final class JvmClassFile implements ClassFile {

    private final String className;
    private final byte[] bytecode;
    private final ClassLoaderHelper classLoaderHelper;

    /**
     * Create an instance.
     *
     * @param className The class name
     * @param bytecode  The bytes that make up the class
     */
    public JvmClassFile(String className, byte[] bytecode) {
        this(className, bytecode, ClassLoaderHelper.INSTANCE);
    }

    private JvmClassFile(String className, byte[] bytecode, ClassLoaderHelper classLoaderHelper) {
        if (className == null) {
            throw new IllegalArgumentException("'className' is null");
        }
        if (bytecode == null) {
            throw new IllegalArgumentException("'bytecode' is null");
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
        byte[] b = bytecode;
        return classLoaderHelper.defineClass(classLoader, className, b, 0, b.length, null);
    }

}
