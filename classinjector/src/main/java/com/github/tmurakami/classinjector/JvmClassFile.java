package com.github.tmurakami.classinjector;

import java.security.ProtectionDomain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class file is for Java VM.
 */
@SuppressWarnings("WeakerAccess")
public final class JvmClassFile implements ClassFile {

    private final String className;
    private final byte[] bytecode;
    private final ProtectionDomain protectionDomain;
    private final ClassLoaderHelper classLoaderHelper;

    /**
     * Instantiates a new instance with the default protection domain.
     *
     * @param className the class name
     * @param bytecode  the bytecode that make up the class
     */
    public JvmClassFile(@Nonnull String className, @Nonnull byte[] bytecode) {
        this(className, bytecode, null);
    }

    /**
     * Instantiates a new instance.
     * If the given protection domain is null, the default domain will be assigned to the class.
     *
     * @param className        the class name
     * @param bytecode         the bytecode that make up the class
     * @param protectionDomain the protection domain, or null
     */
    public JvmClassFile(@Nonnull String className,
                        @Nonnull byte[] bytecode,
                        @Nullable ProtectionDomain protectionDomain) {
        this(className, bytecode, protectionDomain, ClassLoaderHelper.INSTANCE);
    }

    JvmClassFile(String className,
                 byte[] bytecode,
                 ProtectionDomain protectionDomain,
                 ClassLoaderHelper classLoaderHelper) {
        if (className.isEmpty()) {
            throw new IllegalArgumentException("'className' is empty");
        }
        if (bytecode.length == 0) {
            throw new IllegalArgumentException("'bytecode' is empty");
        }
        this.className = className;
        int length = bytecode.length;
        byte[] bytes = new byte[length];
        System.arraycopy(bytecode, 0, bytes, 0, length);
        this.bytecode = bytes;
        this.protectionDomain = protectionDomain;
        this.classLoaderHelper = classLoaderHelper;
    }

    @Nonnull
    @Override
    public Class toClass(@Nonnull ClassLoader classLoader) {
        String name = className;
        ClassLoaderHelper h = classLoaderHelper;
        int dot = name.lastIndexOf('.');
        if (dot > -1) {
            String pkg = name.substring(0, dot);
            if (h.getPackage(classLoader, pkg) == null) {
                try {
                    h.definePackage(classLoader, pkg, null, null, null, null, null, null, null);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        byte[] b = bytecode;
        return h.defineClass(classLoader, name, b, 0, b.length, protectionDomain);
    }

}
