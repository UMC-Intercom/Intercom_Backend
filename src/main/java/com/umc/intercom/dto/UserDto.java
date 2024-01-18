package com.umc.intercom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserDto {

    @Getter
    public static class UserLoginRequestDto {
        String email;
        String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginSuccessDto {
        String token;
    }

    @Builder
    @Getter
    public static class CurrentUserDto {
        private String username;
        private List<String> authorities;
    }

}