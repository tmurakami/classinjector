package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

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
    ClassLoader parent;

    @InjectMocks
    UnsafeClassLoaderHelper testTarget;

    @Test
    public void defineClass() throws Exception {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(unsafe.defineClass("foo.Bar", bytes, 0, bytes.length, classLoader, null)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, null));
    }

    @Test
    public void setParent() throws Exception {
        given(unsafe.objectFieldOffset(parentField)).willReturn(1L);
        testTarget.setParent(classLoader, parent);
        then(unsafe).should().putObject(classLoader, 1L, parent);
    }

}
