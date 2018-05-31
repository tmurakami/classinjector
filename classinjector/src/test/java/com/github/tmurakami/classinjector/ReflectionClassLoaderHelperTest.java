package com.github.tmurakami.classinjector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ReflectionClassLoaderHelperTest {

    @InjectMocks
    private ReflectionClassLoaderHelper testTarget;

    @Mock
    private Field parentField;
    @Mock
    private Method defineClassMethod;
    @Mock
    private ReflectionHelper reflectionHelper;
    @Mock
    private ProtectionDomain protectionDomain;
    @Mock
    private URL url;
    @Mock
    private Package pkg;

    @Test
    public void defineClass_should_call_ClassLoader_defineClass_via_ReflectionHelper() throws Exception {
        ClassLoader classLoader = new ClassLoader() {
        };
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(reflectionHelper.invoke(defineClassMethod, classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain));
        InOrder inOrder = inOrder(reflectionHelper);
        then(reflectionHelper).should(inOrder).setAccessible(defineClassMethod, true);
        then(reflectionHelper).should(inOrder).invoke(defineClassMethod, classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain);
    }

    @Test
    public void setParent_should_set_the_given_ClassLoader_to_ClassLoader_parent_via_ReflectionHelper() throws Exception {
        ClassLoader classLoader = new ClassLoader() {
        };
        ClassLoader parent = new ClassLoader() {
        };
        testTarget.setParent(classLoader, parent);
        InOrder inOrder = inOrder(reflectionHelper);
        then(reflectionHelper).should(inOrder).setAccessible(parentField, true);
        then(reflectionHelper).should(inOrder).set(parentField, classLoader, parent);
    }

}
