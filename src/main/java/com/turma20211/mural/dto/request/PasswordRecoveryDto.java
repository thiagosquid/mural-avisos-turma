package com.turma20211.mural.dto.request;

import lombok.Data;

@Data
public class PasswordRecoveryDto {

    private Long id;
    private String token;
    private String password;
}
