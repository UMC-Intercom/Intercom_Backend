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
    @Setter
    public static class UserUpdateRequestDto{
        String email;
        String password;
        String name;
        String nickname;
        LocalDate birthday;
        String phone;
        Gender gender;
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