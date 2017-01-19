package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOError;
import java.io.IOException;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class StealthClassLoaderTest {

    @Mock
    ClassSource source;
    @Mock
    ClassLoader injectionTarget;
    @Mock
    ClassFile classFile;

    @InjectMocks
    StealthClassLoader testTarget;

    @Test
    public void findClass() throws Exception {
        given(source.getClassFile("foo.Bar")).willReturn(classFile);
        Class<?> c = getClass();
        given(classFile.toClass(injectionTarget)).willReturn(c);
        assertSame(c, testTarget.findClass("foo.Bar"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void findClass_findMyClass() throws Exception {
        testTarget.findClass(StealthClassLoaderTest.class.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void findClass_classFileNotFound() throws Exception {
        testTarget.findClass("foo.Bar");
        then(source).should().getClassFile("foo.Bar");
    }

    @Test(expected = IOError.class)
    public void findClass_ioErrorWhileGettingClassFile() throws Exception {
        given(source.getClassFile("foo.Bar")).willThrow(IOException.class);
        testTarget.findClass("foo.Bar");
    }

    @Test(expected = NoClassDefFoundError.class)
    public void findClass_noClassCreated() throws Exception {
        given(source.getClassFile("foo.Bar")).willReturn(classFile);
        testTarget.findClass("foo.Bar");
    }

}
