package com.github.tmurakami.classinjector.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import dalvik.system.DexFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class DexClassFileTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder(context.getDir("dex_cache", Context.MODE_PRIVATE));

    @Test
    public void toClass() throws Exception {
        String sourcePathName = context.getApplicationInfo().sourceDir;
        String outputPathName = folder.newFile().getCanonicalPath();
        DexFile dexFile = DexFile.loadDex(sourcePathName, outputPathName, 0);
        ClassLoader classLoader = new ClassLoader() {
        };
        Class<?> c = new DexClassFile(C.class.getName(), dexFile).toClass(classLoader);
        assertEquals(C.class.getName(), c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

    private static class C {
    }

}
