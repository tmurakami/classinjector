package com.github.tmurakami.classinjector.android;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class DexClassFileTest {

    @Mock
    private dalvik.system.DexFile dexFile;

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_IllegalArgumentException_if_the_className_is_empty() {
        new DexClassFile("", dexFile);
    }

    @Test
    public void toClass_should_return_the_Class_with_the_given_name() {
        Class<?> c = getClass();
        given(dexFile.entries()).willReturn(Collections.enumeration(Collections.singleton("foo.Bar")));
        ClassLoader classLoader = new ClassLoader() {
        };
        given(dexFile.loadClass("foo.Bar", classLoader)).willReturn(c);
        assertSame(c, new DexClassFile("foo.Bar", dexFile).toClass(classLoader));
    }

}
