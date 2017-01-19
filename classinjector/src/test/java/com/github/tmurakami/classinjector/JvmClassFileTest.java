package com.github.tmurakami.classinjector;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class JvmClassFileTest {

    @Test
    public void toClass() throws Exception {
        String name = C.class.getName();
        InputStream in = getClass().getResourceAsStream('/' + name.replace('.', '/') + ".class");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        for (int l; (l = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, l);
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