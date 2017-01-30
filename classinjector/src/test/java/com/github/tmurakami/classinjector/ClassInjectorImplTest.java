package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ClassInjectorImplTest {

    @InjectMocks
    ClassInjectorImpl testTarget;

    @Mock
    ClassSource source;
    @Mock
    ClassLoaderFactory classLoaderFactory;
    @Mock
    ClassLoaderHelper classLoaderHelper;
    @Mock
    StealthClassLoader stealthClassLoader;

    @Test
    public void into() throws Exception {
        ClassLoader target = mock(ClassLoader.class);
        ClassLoader parent = mock(ClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        given(classLoaderFactory.newClassLoader(parent, source, target)).willReturn(stealthClassLoader);
        testTarget.into(target);
        then(classLoaderHelper).should().setParent(target, stealthClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_parentIsNull() throws Exception {
        testTarget.into(mock(ClassLoader.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_targetIsStealthClassLoader() throws Exception {
        ClassLoader target = mock(StealthClassLoader.class);
        ClassLoader parent = mock(ClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        testTarget.into(target);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_parentIsStealthClassLoader() throws Exception {
        ClassLoader target = mock(ClassLoader.class);
        ClassLoader parent = mock(StealthClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        testTarget.into(target);
    }

}
