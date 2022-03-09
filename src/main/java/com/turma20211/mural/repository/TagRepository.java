package com.turma20211.mural.repository;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(value = "SELECT * FROM TB_TAG t WHERE t.class_id = ?1", nativeQuery = true)
    List<Tag> findByClass(Long classId);

    Optional<Tag> findByDescription(String description);

    @Query(value = "SELECT * FROM TB_TAG t WHERE t.description = ?1 AND t.class_id = ?2", nativeQuery = true)
    Optional<Tag> findByDescriptionAndAClass(String description, Long classId);
}
