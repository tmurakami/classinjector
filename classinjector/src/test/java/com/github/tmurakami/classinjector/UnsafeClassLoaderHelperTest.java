package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class UnsafeClassLoaderHelperTest {

    @InjectMocks
    private UnsafeClassLoaderHelper testTarget;

    @Mock
    private Field parentField;
    @Mock
    private UnsafeWrapper unsafeWrapper;
    @Mock
    private ProtectionDomain protectionDomain;

    @Test
    public void defineClass_should_simply_call_UnsafeWrapper_defineClass() {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        ClassLoader classLoader = new ClassLoader() {
        };
        given(unsafeWrapper.defineClass("foo.Bar", bytes, 0, bytes.length, classLoader, protectionDomain)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain));
    }

    @Test
    public void setParent_should_set_the_given_ClassLoader_to_ClassLoader_parent_via_UnsafeWrapper() {
        given(unsafeWrapper.objectFieldOffset(parentField)).willReturn(1L);
        ClassLoader classLoader = new ClassLoader() {
        };
        ClassLoader parent = new ClassLoader() {
        };
        testTarget.setParent(classLoader, parent);
        then(unsafeWrapper).should().putObject(classLoader, 1L, parent);
    }

}
