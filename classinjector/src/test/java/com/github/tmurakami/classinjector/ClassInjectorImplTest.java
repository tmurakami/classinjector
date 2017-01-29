package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
    ClassLoader target;
    @Mock
    ClassLoader parent;
    @Mock
    StealthClassLoader stealthClassLoader;

    @Test
    public void into() throws Exception {
        given(classLoaderHelper.getParent(target)).willReturn(parent);
        given(classLoaderFactory.newClassLoader(parent, source, target)).willReturn(stealthClassLoader);
        testTarget.into(target);
        then(classLoaderHelper).should().setParent(target, stealthClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_alreadyInjected() throws Exception {
        given(classLoaderHelper.getParent(target)).willReturn(stealthClassLoader);
        testTarget.into(target);
    }

}
