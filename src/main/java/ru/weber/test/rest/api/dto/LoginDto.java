package ru.weber.test.rest.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull
    private String login;
    @NotNull
    private String password;
}
