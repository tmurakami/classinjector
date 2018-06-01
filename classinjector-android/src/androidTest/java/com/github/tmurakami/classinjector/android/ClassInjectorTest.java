package com.github.tmurakami.classinjector.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import com.github.tmurakami.classinjector.ClassFile;
import com.github.tmurakami.classinjector.ClassInjector;
import com.github.tmurakami.classinjector.ClassSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.MyAndroidTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ClassInjectorTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder(context.getCacheDir());

    @SuppressWarnings("deprecation")
    @Test
    public void a_class_should_be_injected_into_a_class_loader() throws Exception {
        String src = context.getApplicationInfo().sourceDir;
        String out = folder.newFile().getCanonicalPath();
        final dalvik.system.DexFile dexFile = dalvik.system.DexFile.loadDex(src, out, 0);
        ClassLoader classLoader = new ClassLoader() {
        };
        ClassInjector.from(new ClassSource() {
            @NonNull
            @Override
            public ClassFile getClassFile(@NonNull String className) {
                return new DexClassFile(className, dexFile);
            }
        }).into(classLoader);
        assertEquals("com.github.tmurakami.classinjector.InjectorClassLoader", classLoader.getParent().getClass().getName());
        String className = MyAndroidTestClass.class.getName();
        Class<?> c = classLoader.loadClass(className);
        assertEquals(className, c.getName());
        assertSame(classLoader, c.getClassLoader());
    }

}
