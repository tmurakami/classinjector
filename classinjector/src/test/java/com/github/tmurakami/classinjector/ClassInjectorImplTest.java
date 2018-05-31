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
        ClassLoader parent = new ClassLoader() {
        };
        ClassLoader target = new ClassLoader(parent) {
        };
        given(injectorClassLoaderFactory.newInjectorClassLoader(parent, source, target)).willReturn(stealthClassLoader);
        testTarget.into(target);
        then(classLoaderHelper).should().setParent(target, stealthClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_null() {
        testTarget.into(new ClassLoader(null) {
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_target_is_an_InjectorClassLoader() {
        ClassLoader parent = new ClassLoader() {
        };
        ClassLoader target = mock(InjectorClassLoader.class);
        given(target.getParent()).willReturn(parent);
        testTarget.into(target);
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_an_InjectorClassLoader() {
        ClassLoader parent = mock(InjectorClassLoader.class);
        ClassLoader target = new ClassLoader(parent) {
        };
        testTarget.into(target);
    }

}
