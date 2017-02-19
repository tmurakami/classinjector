package com.github.tmurakami.classinjector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOError;
import java.io.IOException;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class StealthClassLoaderTest {

    private StealthClassLoader testTarget;

    @Mock
    ClassLoader parent;
    @Mock
    ClassSource source;
    @Mock
    ClassLoader injectionTarget;
    @Mock
    ClassFile classFile;

    @Before
    public void setUp() throws Exception {
        testTarget = new StealthClassLoader(parent, source, injectionTarget);
    }

    @Test
    public void the_findClass_method_should_return_the_Class_with_the_given_name() throws Exception {
        given(source.getClassFile("foo.Bar")).willReturn(classFile);
        Class<?> c = getClass();
        given(classFile.toClass(injectionTarget)).willReturn(c);
        assertSame(c, testTarget.findClass("foo.Bar"));
    }

    @Test(expected = IllegalStateException.class)
    public void the_findClass_method_should_throw_an_IllegalStateException_if_called_recursively() throws Exception {
        given(source.getClassFile("foo.Bar")).willReturn(classFile);
        given(classFile.toClass(injectionTarget)).will(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                testTarget.findClass("foo.Bar");
                return null;
            }
        });
        testTarget.findClass("foo.Bar");
    }

    @Test(expected = ClassNotFoundException.class)
    public void the_findClass_method_should_throw_a_ClassNotFoundException_if_the_class_name_belongs_to_the_ClassInjector_package() throws Exception {
        testTarget.findClass(StealthClassLoaderTest.class.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void the_findClass_method_should_throw_a_ClassNotFoundException_if_the_class_with_the_given_name_cannot_be_found() throws Exception {
        testTarget.findClass("foo.Bar");
        then(source).should().getClassFile("foo.Bar");
    }

    @Test(expected = IOError.class)
    public void the_findClass_method_should_throw_an_IOError_if_an_IO_error_occurs_while_getting_a_ClassFile() throws Exception {
        given(source.getClassFile("foo.Bar")).willThrow(IOException.class);
        testTarget.findClass("foo.Bar");
    }

}
