package com.turma20211.mural.service;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.Tag;
import com.turma20211.mural.repository.TagRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @InjectMocks
    private ClassService classService;

    @Mock
    Class aClass;

    @Test
    @DisplayName("Get All Tag's")
    void whenGetAllIsCalledShouldBeReturnAllTags() {
        //when
        Mockito.when(tagRepository.findAll()).thenReturn(new ArrayList<Tag>());
        //then
        List<Tag> foundListTags = tagService.getAll();

        MatcherAssert.assertThat(foundListTags.isEmpty(), is(true));
        MatcherAssert.assertThat(foundListTags.getClass(), is(ArrayList.class));
    }
}