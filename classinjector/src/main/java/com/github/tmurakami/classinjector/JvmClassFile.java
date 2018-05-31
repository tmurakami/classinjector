package com.github.tmurakami.classinjector;

import java.security.ProtectionDomain;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The {@link ClassFile} for Java VM.
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
        int length = bytecode.length;
        if (length == 0) {
            throw new IllegalArgumentException("'bytecode' is empty");
        }
        this.className = className;
        this.bytecode = Arrays.copyOf(bytecode, length);
        this.protectionDomain = protectionDomain;
        this.classLoaderHelper = classLoaderHelper;
    }

    @Nonnull
    @Override
    public Class toClass(@Nonnull ClassLoader classLoader) {
        byte[] b = bytecode;
        return classLoaderHelper.defineClass(classLoader,
                                             className,
                                             b,
                                             0,
                                             b.length,
                                             protectionDomain);
    }

}
