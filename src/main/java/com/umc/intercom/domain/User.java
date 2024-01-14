package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import com.umc.intercom.domain.common.enums.Gender;
import com.umc.intercom.domain.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String username;

    @Column(nullable = false, length = 15)
    private String password;

    private LocalDate birthday;

    @Column(nullable = false, length = 50)
    private String email;

    @ColumnDefault("0")
    private Integer coin;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    private Role role;
}
