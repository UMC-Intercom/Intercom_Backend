package com.umc.intercom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class UserDto {

    @Getter
    public static class UserLoginRequestDto {
        String email;
        String password;
    }

    @Getter
    @Setter
    public static class UserWithdrawRequestDto{
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