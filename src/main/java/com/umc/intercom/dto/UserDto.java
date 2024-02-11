package com.umc.intercom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.umc.intercom.domain.common.enums.Gender;

public class UserDto {

    @Getter
    public static class UserRequestDto {
        String email;
        String password;
    }

    @Getter
    public static class ValidateRequestDto {
        String password;
    }

    @Getter
    public static class FindEmailRequestDto {
        String phone;
    }

    @Getter
    @Setter
    public static class FindEmailResponseDto {
        String name;
        String email;

        public FindEmailResponseDto(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    @Getter
    public static class EmailRequestDto {
        String email;
    }

    @Getter
    @Setter
    public static class SignUpRequestDto{
        String email;
        String password;
        String name;
        String nickname;
        LocalDate birthday;
        String phone;
        String gender;
    }

    @Getter
    @Setter
    public static class UserUpdateRequestDto{
        String password;
        String name;
        String nickname;
        LocalDate birthday;
        String phone;
        String gender;
    }
    
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginSuccessDto {
        private String name;
        private String nickname;
        private LocalDate birthday;
        private String gender;
        private String defaultProfile;
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