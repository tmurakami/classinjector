package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import sun.misc.Unsafe;

/**
 * The {@link ClassFile} for Java VM.
 */
@SuppressWarnings("WeakerAccess")
public final class JvmClassFile implements ClassFile {

    private static final Method DEFINE_CLASS_METHOD;

    static {
        try {
            DEFINE_CLASS_METHOD = ClassLoader.class.getDeclaredMethod("defineClass",
                                                                      String.class,
                                                                      byte[].class,
                                                                      int.class,
                                                                      int.class,
                                                                      ProtectionDomain.class);
        } catch (NoSuchMethodException e) {
            throw (Error) new NoSuchMethodError(e.getMessage()).initCause(e);
        }
    }

    private final String className;
    private final byte[] bytecode;
    private final ProtectionDomain protectionDomain;

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
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public Class toClass(@Nonnull ClassLoader classLoader) {
        String name = className;
        byte[] b = bytecode;
        int off = 0;
        int len = b.length;
        ProtectionDomain pd = protectionDomain;
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
            return unsafe.defineClass(name, b, off, len, classLoader, pd);
        } catch (Throwable ignored) {
        }
        Method defineClassMethod = DEFINE_CLASS_METHOD;
        try {
            while (true) {
                defineClassMethod.setAccessible(true);
                try {
                    return (Class) defineClassMethod.invoke(classLoader, name, b, off, len, pd);
                } catch (IllegalAccessException ignored) {
                }
            }
        } catch (Throwable t) {
            throw new IllegalStateException("This Java version is not supported");
        }
    }

}
