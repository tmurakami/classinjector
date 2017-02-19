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

    @InjectMocks
    UnsafeClassLoaderHelper testTarget;

    @Mock
    Field parentField;
    @Mock
    UnsafeWrapper unsafeWrapper;
    @Mock
    ClassLoader classLoader;
    @Mock
    ProtectionDomain protectionDomain;
    @Mock
    URL url;
    @Mock
    ClassLoader parent;

    @Test
    public void the_defineClass_method_should_simply_call_the_UnsafeWrapper_defineClass_method() throws Exception {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(unsafeWrapper.defineClass("foo.Bar", bytes, 0, bytes.length, classLoader, protectionDomain)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain));
    }

    @Test
    public void the_definePackage_method_should_return_null() throws Exception {
        assertNull(testTarget.definePackage(classLoader, "foo", "a", "b", "c", "d", "e", "f", url));
    }

    @Test
    public void the_getPackage_method_should_return_null() throws Exception {
        assertNull(testTarget.getPackage(classLoader, "foo"));
    }

    @Test
    public void the_setParent_method_should_set_the_ClassLoader_parent_field_to_the_given_parent_via_the_UnsafeWrapper() throws Exception {
        given(unsafeWrapper.objectFieldOffset(parentField)).willReturn(1L);
        testTarget.setParent(classLoader, parent);
        then(unsafeWrapper).should().putObject(classLoader, 1L, parent);
    }

}
