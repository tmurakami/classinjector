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
    private ClassLoaderFactory classLoaderFactory;
    @Mock
    private ClassLoaderHelper classLoaderHelper;
    @Mock
    private StealthClassLoader stealthClassLoader;

    @Test
    public void into_should_replace_the_parent_with_the_StealthClassLoader() throws Exception {
        ClassLoader target = mock(ClassLoader.class);
        ClassLoader parent = mock(ClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        given(classLoaderFactory.newClassLoader(parent, source, target)).willReturn(stealthClassLoader);
        testTarget.into(target);
        then(classLoaderHelper).should().setParent(target, stealthClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_null() throws Exception {
        testTarget.into(mock(ClassLoader.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_target_is_a_StealthClassLoader() throws Exception {
        ClassLoader target = mock(StealthClassLoader.class);
        ClassLoader parent = mock(ClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        testTarget.into(target);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_a_StealthClassLoader() throws Exception {
        ClassLoader target = mock(ClassLoader.class);
        ClassLoader parent = mock(StealthClassLoader.class);
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        testTarget.into(target);
    }

}
