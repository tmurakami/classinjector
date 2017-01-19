package com.github.tmurakami.classinjector.android;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import dalvik.system.DexFile;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DexClassFileTest {

    @Mock
    DexFile dexFile;
    @Mock
    ClassLoader classLoader;

    @Test
    public void toClass() throws Exception {
        Class<?> c = getClass();
        given(dexFile.loadClass("foo.Bar", classLoader)).willReturn(c);
        assertSame(c, new DexClassFile("foo.Bar", dexFile).toClass(classLoader));
    }

}
