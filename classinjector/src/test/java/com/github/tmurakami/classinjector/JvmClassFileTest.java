package com.github.tmurakami.classinjector;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class JvmClassFileTest {

    @Test(expected = IllegalArgumentException.class)
    public void the_constructor_should_throw_an_IllegalArgumentException_if_the_class_name_is_empty() throws Exception {
        new JvmClassFile("", new byte[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void the_constructor_should_throw_an_IllegalArgumentException_if_the_bytecode_is_empty() throws Exception {
        new JvmClassFile("foo.Bar", new byte[0]);
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    @Test
    public void the_toClass_method_should_return_the_Class_with_the_given_name() throws Exception {
        String name = C.class.getName();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = getClass().getResourceAsStream('/' + name.replace('.', '/') + ".class");
        try {
            byte[] buffer = new byte[16384];
            for (int l; (l = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, l);
            }
        } finally {
            in.close();
        }
        ClassLoader classLoader = new ClassLoader() {
        };
        Class<?> c = new JvmClassFile(name, out.toByteArray()).toClass(classLoader);
        assertEquals(C.class.getName(), c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

    private static class C {
    }

}
