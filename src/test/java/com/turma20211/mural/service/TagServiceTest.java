package com.turma20211.mural.service;

import com.turma20211.mural.model.Tag;
import com.turma20211.mural.repository.TagRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    void whenGetAllIsCalledShouldBeReturnAllTags() {
        //when
        Mockito.when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        List<Tag> foundListTags = tagService.getAll();

        MatcherAssert.assertThat(foundListTags.isEmpty(), is(true));
    }
}