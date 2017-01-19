package com.github.tmurakami.classinjector.android;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import dalvik.system.DexFile;

@SuppressWarnings("deprecation")
public class DexClassFileTest extends android.test.AndroidTestCase {

    private List<File> files;
    private File outputFile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        File cacheDir = getContext().getDir("dex_cache", Context.MODE_PRIVATE);
        File outputFile = new File(cacheDir, "classes.dex");
        files = Arrays.asList(outputFile, cacheDir);
        this.outputFile = outputFile;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for (File f : files) {
            if (f.exists() && !f.delete()) {
                Log.w(getName(), "Cannot delete " + f);
            }
        }
    }

    public void testToClass() throws Exception {
        String sourcePathName = getContext().getApplicationInfo().sourceDir;
        DexFile dexFile = DexFile.loadDex(sourcePathName, outputFile.getCanonicalPath(), 0);
        ClassLoader classLoader = new ClassLoader() {
        };
        Class<?> c = new DexClassFile(C.class.getName(), dexFile).toClass(classLoader);
        assertEquals(C.class.getName(), c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

    private static class C {
    }

}
