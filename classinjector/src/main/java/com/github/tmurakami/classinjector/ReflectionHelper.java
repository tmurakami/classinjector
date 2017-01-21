package com.github.tmurakami.classinjector;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

final class ReflectionHelper {

    static final ReflectionHelper INSTANCE = new ReflectionHelper();

    private ReflectionHelper() {
    }

    Field getDeclaredField(final Class<?> c, final String name) {
        Throwable t;
        try {
            if (System.getSecurityManager() == null) {
                return c.getDeclaredField(name);
            } else {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() {
                    @Override
                    public Field run() throws Exception {
                        return c.getDeclaredField(name);
                    }
                });
            }
        } catch (NoSuchFieldException e) {
            t = e;
        } catch (PrivilegedActionException e) {
            t = e.getCause();
        }
        if (t instanceof NoSuchFieldException) {
            throw new NoSuchFieldError(t.getMessage());
        } else {
            throw new IllegalStateException("Cannot get " + c.getName() + '#' + name, t);
        }
    }

    Method getDeclaredMethod(final Class<?> c, final String name, final Class<?>... parameterTypes) {
        Throwable t;
        try {
            if (System.getSecurityManager() == null) {
                return c.getDeclaredMethod(name, parameterTypes);
            } else {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() {
                    @Override
                    public Method run() throws Exception {
                        return c.getDeclaredMethod(name, parameterTypes);
                    }
                });
            }
        } catch (NoSuchMethodException e) {
            t = e;
        } catch (PrivilegedActionException e) {
            t = e.getCause();
        }
        if (t instanceof NoSuchMethodException) {
            throw new NoSuchMethodError(t.getMessage());
        } else {
            throw new IllegalStateException("Cannot get " + c.getName() + '#' + name, t);
        }
    }

    Method getMethod(final Class<?> c, final String name, final Class<?>... parameterTypes) {
        Throwable t;
        try {
            if (System.getSecurityManager() == null) {
                return c.getDeclaredMethod(name, parameterTypes);
            } else {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() {
                    @Override
                    public Method run() throws Exception {
                        return c.getMethod(name, parameterTypes);
                    }
                });
            }
        } catch (NoSuchMethodException e) {
            t = e;
        } catch (PrivilegedActionException e) {
            t = e.getCause();
        }
        if (t instanceof NoSuchMethodException) {
            throw new NoSuchMethodError(t.getMessage());
        } else {
            throw new IllegalStateException("Cannot get " + c.getName() + '#' + name, t);
        }
    }

    Object get(Field field, Object o) {
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    Object invoke(Method method, Object o, Object... parameters) {
        try {
            return method.invoke(o, parameters);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new IllegalStateException("Cannot invoke "
                        + method.getDeclaringClass().getName() + '#' + method.getName(), cause);
            }
        }
    }

    void set(Field field, Object o, Object value) {
        try {
            field.set(o, value);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    void setAccessible(final AccessibleObject o, final boolean accessible) {
        if (System.getSecurityManager() == null) {
            o.setAccessible(accessible);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    o.setAccessible(accessible);
                    return null;
                }
            });
        }
    }

}
