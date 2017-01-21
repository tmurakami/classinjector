package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.net.URL;
import java.security.ProtectionDomain;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class UnsafeClassLoaderHelperTest {

    @Mock
    Field parentField;
    @Mock
    Unsafe unsafe;
    @Mock
    ClassLoader classLoader;
    @Mock
    ProtectionDomain protectionDomain;
    @Mock
    URL url;
    @Mock
    ClassLoader parent;

    @InjectMocks
    UnsafeClassLoaderHelper testTarget;

    @Test
    public void defineClass() throws Exception {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(unsafe.defineClass("foo.Bar", bytes, 0, bytes.length, classLoader, protectionDomain)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain));
    }

    @Test
    public void definePackage() throws Exception {
        assertNull(testTarget.definePackage(classLoader, "foo", "a", "b", "c", "d", "e", "f", url));
    }

    @Test
    public void getPackage() throws Exception {
        assertNull(testTarget.getPackage(classLoader, "foo"));
    }

    @Test
    public void setParent() throws Exception {
        given(unsafe.objectFieldOffset(parentField)).willReturn(1L);
        testTarget.setParent(classLoader, parent);
        then(unsafe).should().putObject(classLoader, 1L, parent);
    }

}
