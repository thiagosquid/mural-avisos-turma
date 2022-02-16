package com.turma20211.mural.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoveryDto {

    private Long id;
    private String token;
    private String password;
}
