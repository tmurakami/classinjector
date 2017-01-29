package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.ProtectionDomain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class JvmClassFileTest {

    @Mock
    ProtectionDomain protectionDomain;
    @Mock
    ClassLoaderHelper classLoaderHelper;
    @Mock
    ClassLoader classLoader;

    @Test(expected = IllegalArgumentException.class)
    public void _new_emptyName() throws Exception {
        new JvmClassFile("", new byte[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void _new_emptyBytecode() throws Exception {
        new JvmClassFile("foo.Bar", new byte[0]);
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    @Test
    public void toClass() throws Exception {
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

    @Test
    public void toClass_useMock() throws Exception {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(classLoaderHelper.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain)).willReturn(c);
        assertSame(c, new JvmClassFile("foo.Bar", bytes, protectionDomain, classLoaderHelper).toClass(classLoader));
        InOrder inOrder = inOrder(classLoaderHelper);
        then(classLoaderHelper).should(inOrder).getPackage(classLoader, "foo");
        then(classLoaderHelper).should(inOrder).definePackage(classLoader, "foo", null, null, null, null, null, null, null);
        then(classLoaderHelper).should(inOrder).defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain);
    }

    private static class C {
    }

}
