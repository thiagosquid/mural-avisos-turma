package com.muraldaturma.api.repository;

import com.muraldaturma.api.model.Tag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    // JUnit test for save tag operation
    /*@DisplayName("JUnit test for save tag operation")
    @Test
    void givenTagObject_whenSave_thenReturnSavedTag() {
        // given - precondition or setup
        Tag tag = Tag.builder()
                .description("Atividade")
                .build();

        // when - action or the behaviour that we are going test
        Tag savedTag = tagRepository.save(tag);

        // then - verify the output
        Assertions.assertThat(savedTag).isNotNull();
        Assertions.assertThat(savedTag.getId()).isGreaterThan(0);

    }*/

    /*
    @Test
    void givenTagList_whenFindAll_thenReturnTagList() {
        // given - precondition or setup
        Tag tag = Tag.builder()
                .description("Atividade")
                .build();
        Tag tag2 = Tag.builder()
                .description("Prova")
                .build();
        tagRepository.save(tag);
        tagRepository.save(tag2);

        // when - action or the behaviour that we are going test
        List<Tag> tagList = tagRepository.findAll();

        // then - verify the output
        Assertions.assertThat(tagList).isNotNull();
        Assertions.assertThat(tagList.size()).isEqualTo(2);
        Assertions.assertThat(tagList).isNotEmpty();

    }*/

    // JUnit test for
    @Test
    void given_when_then() {
        // given - precondition or setup

        // when - action or the behaviour that we are going test

        // then - verify the output

    }

}