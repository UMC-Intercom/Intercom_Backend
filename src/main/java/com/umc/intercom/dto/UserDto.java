package com.umc.intercom.dto;

import lombok.*;

import java.util.List;

public class UserDto {

    @Getter
    public static class UserRequestDto {
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
//        private String username;
        private String email;
        private String name;
        private String nickname;
        private List<String> authorities;
    }

}