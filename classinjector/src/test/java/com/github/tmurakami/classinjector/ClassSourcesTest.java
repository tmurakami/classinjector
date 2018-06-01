package com.github.tmurakami.classinjector;

import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ClassSourcesTest {

    @Mock
    private ClassSource source;
    @Mock
    private ClassFile classFile;

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_IllegalArgumentException_if_the_sources_are_empty() {
        new ClassSources(Collections.<ClassSource>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_IllegalArgumentException_if_the_sources_contain_null() {
        new ClassSources(Collections.<ClassSource>singleton(null));
    }

    @Test
    public void getClassFile_should_return_the_ClassFile_with_the_given_name() throws Exception {
        given(source.getClassFile("foo.Bar")).willReturn(classFile);
        assertSame(classFile, new ClassSources(Collections.singleton(source)).getClassFile("foo.Bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClassFile_should_throw_IllegalArgumentException_if_the_className_is_empty()
            throws Exception {
        new ClassSources(Collections.singleton(source)).getClassFile("");
    }

    @Test
    public void getClassFile_should_return_null_if_no_DexFile_with_the_given_name_can_be_found()
            throws Exception {
        assertNull(new ClassSources(Collections.singleton(source)).getClassFile("foo.Bar"));
    }

}
