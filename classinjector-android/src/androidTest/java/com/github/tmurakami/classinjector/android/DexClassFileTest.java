package com.github.tmurakami.classinjector.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class DexClassFileTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder(context.getCacheDir());

    @SuppressWarnings("deprecation")
    @Test
    public void toClass_should_return_the_Class_with_the_given_name() throws Exception {
        String sourcePathName = context.getApplicationInfo().sourceDir;
        String outputPathName = folder.newFile().getCanonicalPath();
        dalvik.system.DexFile dexFile = dalvik.system.DexFile.loadDex(sourcePathName, outputPathName, 0);
        ClassLoader classLoader = new ClassLoader() {
        };
        Class<?> c = new DexClassFile(C.class.getName(), dexFile).toClass(classLoader);
        assertEquals(C.class.getName(), c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

    private static class C {
    }

}
