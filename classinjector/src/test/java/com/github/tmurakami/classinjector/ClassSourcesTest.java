package com.github.tmurakami.classinjector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ClassSourcesTest {

    @Mock
    ClassSource source;
    @Mock
    ClassFile classFile;

    @Test(expected = IllegalArgumentException.class)
    public void _new_emptySources() {
        new ClassSources(Collections.<ClassSource>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void _new_containsNullSource() {
        new ClassSources(Collections.<ClassSource>singleton(null));
    }

    @Test
    public void getClassFile() throws Exception {
        given(source.getClassFile("foo.Bar")).willReturn(classFile);
        assertSame(classFile, new ClassSources(Collections.singleton(source)).getClassFile("foo.Bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClassFile_emptyClassName() throws Exception {
        new ClassSources(Collections.singleton(source)).getClassFile("");
    }

    @Test
    public void getClassFile_classNotFound() throws Exception {
        assertNull(new ClassSources(Collections.singleton(source)).getClassFile("foo.Bar"));
    }

}
