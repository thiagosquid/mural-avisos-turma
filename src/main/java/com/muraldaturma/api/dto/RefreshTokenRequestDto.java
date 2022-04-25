package com.muraldaturma.api.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {

    @NonNull
    private String refreshToken;
}
