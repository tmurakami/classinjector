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

    @Mock
    ClassSource source;
    @Mock
    ClassLoaderFactory classLoaderFactory;
    @Mock
    ClassLoaderHelper classLoaderHelper;
    @Mock
    ClassLoader injectionTarget;
    @Mock
    StealthClassLoader stealthClassLoader;

    @InjectMocks
    ClassInjectorImpl testTarget;

    @Test
    public void into() throws Exception {
        given(classLoaderFactory.newClassLoader(source, injectionTarget)).willReturn(stealthClassLoader);
        testTarget.into(injectionTarget);
        then(classLoaderHelper).should().setParent(injectionTarget, stealthClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_nullTarget() throws Exception {
        testTarget.into(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_alreadyInjected() throws Exception {
        given(injectionTarget.getParent()).willReturn(stealthClassLoader);
        testTarget.into(injectionTarget);
    }

}
