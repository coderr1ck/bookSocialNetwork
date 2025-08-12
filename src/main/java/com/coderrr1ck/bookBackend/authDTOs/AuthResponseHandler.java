package com.coderrr1ck.bookBackend.authDTOs;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthResponseHandler {
    @NotNull
    private String token;
}
