package com.github.tmurakami.classinjector;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.junit.Test;
import test.MyTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class JvmClassFileTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_IllegalArgumentException_if_the_className_is_empty() {
        new JvmClassFile("", new byte[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_IllegalArgumentException_if_the_bytecode_is_empty() {
        new JvmClassFile("foo.Bar", new byte[0]);
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    @Test
    public void toClass_should_return_the_Class_with_the_given_name() throws Exception {
        String name = MyTestClass.class.getName();
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
        ClassLoader classLoader = new ClassLoader(null) {
        };
        Class<?> c = new JvmClassFile(name, out.toByteArray()).toClass(classLoader);
        assertEquals(MyTestClass.class.getName(), c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

}
