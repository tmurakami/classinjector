package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class ReflectionClassLoaderHelperTest {

    @Mock
    Field parentField;
    @Mock(name = "defineClassMethod")
    Method defineClassMethod;
    @Mock(name = "definePackageMethod")
    Method definePackageMethod;
    @Mock(name = "getPackageMethod")
    Method getPackageMethod;
    @Mock
    ClassLoader classLoader;
    @Mock
    ProtectionDomain protectionDomain;
    @Mock
    URL url;
    @Mock
    Package pkg;
    @Mock
    ClassLoader parent;

    @InjectMocks
    ReflectionClassLoaderHelper testTarget;

    @Test
    public void defineClass() throws Exception {
        byte[] bytes = "abc".getBytes();
        Class<?> c = getClass();
        given(defineClassMethod.invoke(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain)).willReturn(c);
        assertSame(c, testTarget.defineClass(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain));
        InOrder inOrder = inOrder(defineClassMethod);
        then(defineClassMethod).should(inOrder).setAccessible(true);
        then(defineClassMethod).should(inOrder).invoke(classLoader, "foo.Bar", bytes, 0, bytes.length, protectionDomain);
    }

    @Test
    public void definePackage() throws Exception {
        given(definePackageMethod.invoke(classLoader, "foo", "a", "b", "c", "d", "e", "f", url)).willReturn(pkg);
        assertSame(pkg, testTarget.definePackage(classLoader, "foo", "a", "b", "c", "d", "e", "f", url));
        InOrder inOrder = inOrder(definePackageMethod);
        then(definePackageMethod).should(inOrder).setAccessible(true);
        then(definePackageMethod).should(inOrder).invoke(classLoader, "foo", "a", "b", "c", "d", "e", "f", url);
    }

    @Test
    public void getPackage() throws Exception {
        given(getPackageMethod.invoke(classLoader, "foo")).willReturn(pkg);
        assertSame(pkg, testTarget.getPackage(classLoader, "foo"));
        InOrder inOrder = inOrder(getPackageMethod);
        then(getPackageMethod).should(inOrder).setAccessible(true);
        then(getPackageMethod).should(inOrder).invoke(classLoader, "foo");
    }

    @Test
    public void setParent() throws Exception {
        testTarget.setParent(classLoader, parent);
        InOrder inOrder = inOrder(parentField);
        then(parentField).should(inOrder).setAccessible(true);
        then(parentField).should(inOrder).set(classLoader, parent);
    }

}
