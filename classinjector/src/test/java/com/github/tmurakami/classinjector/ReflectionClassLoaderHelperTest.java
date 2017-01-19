package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class ReflectionClassLoaderHelperTest {

    @Mock
    Field parentField;
    @Mock
    Method defineClassMethod;
    @Mock
    ClassLoader classLoader;
    @Mock
    ClassLoader parent;

    @InjectMocks
    ReflectionClassLoaderHelper testTarget;

    @Test
    public void defineClass() throws Exception {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(defineClassMethod.invoke(classLoader, "foo.Bar", bytes, 0, bytes.length, null)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, null));
        InOrder inOrder = inOrder(defineClassMethod);
        then(defineClassMethod).should(inOrder).setAccessible(true);
        then(defineClassMethod).should(inOrder).invoke(classLoader, "foo.Bar", bytes, 0, bytes.length, null);
    }

    @Test
    public void setParent() throws Exception {
        testTarget.setParent(classLoader, parent);
        InOrder inOrder = inOrder(parentField);
        then(parentField).should(inOrder).setAccessible(true);
        then(parentField).should(inOrder).set(classLoader, parent);
    }

    @Test(expected = IllegalAccessError.class)
    public void setParent_illegalAccessError() throws Exception {
        Field parentField = spy(ClassLoader.class.getDeclaredField("parent"));
        willDoNothing().given(parentField).setAccessible(true);
        new ReflectionClassLoaderHelper(parentField, defineClassMethod).setParent(classLoader, parent);
    }

}
