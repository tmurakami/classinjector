package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ClassInjectorImplTest {

    @InjectMocks
    private ClassInjectorImpl testTarget;

    @Mock
    private ClassSource source;
    @Mock
    private ClassLoaderHelper classLoaderHelper;
    @Mock
    private InjectorClassLoaderFactory injectorClassLoaderFactory;
    @Mock
    private InjectorClassLoader stealthClassLoader;

    @Test
    public void into_should_replace_the_parent_with_the_InjectorClassLoader() {
        ClassLoader target = mock(ClassLoader.class);
        ClassLoader parent = mock(ClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        given(injectorClassLoaderFactory.newInjectorClassLoader(parent, source, target)).willReturn(stealthClassLoader);
        testTarget.into(target);
        then(classLoaderHelper).should().setParent(target, stealthClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_null() {
        testTarget.into(mock(ClassLoader.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_target_is_an_InjectorClassLoader() {
        ClassLoader target = mock(InjectorClassLoader.class);
        ClassLoader parent = mock(ClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        testTarget.into(target);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_an_InjectorClassLoader() {
        ClassLoader target = mock(ClassLoader.class);
        ClassLoader parent = mock(InjectorClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        testTarget.into(target);
    }

}
