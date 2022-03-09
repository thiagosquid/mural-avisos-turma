package com.turma20211.mural.builder;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.User;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class ClassBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "ads20211";

    @Builder.Default
    private List<User> users = new ArrayList<>();

    public Class toClass(){
        return new Class(null, name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

}

