package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ClassInjectorImplTest {

    @InjectMocks
    private ClassInjectorImpl testTarget;

    @Mock
    private InjectorClassLoaderFactory injectorClassLoaderFactory;
    @Mock
    private InjectorClassLoader injectorClassLoader;

    @Test
    public void into_should_replace_the_parent_with_the_InjectorClassLoader() {
        ClassLoader target = new ClassLoader(null) {
        };
        given(injectorClassLoaderFactory.newInjectorClassLoader(null, target)).willReturn(injectorClassLoader);
        testTarget.into(target);
        assertSame(injectorClassLoader, target.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_target_is_an_InjectorClassLoader() {
        testTarget.into(mock(InjectorClassLoader.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void into_should_throw_IllegalArgumentException_if_the_parent_is_an_InjectorClassLoader() {
        testTarget.into(new ClassLoader(mock(InjectorClassLoader.class)) {
        });
    }

}
