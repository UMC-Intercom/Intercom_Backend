package com.umc.intercom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.umc.intercom.domain.common.enums.Gender;
import com.umc.intercom.domain.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;   // 이메일(= 아이디)

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 15)
    private String name;

    @Getter
    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false, length = 13)
    private String phone;

    @ColumnDefault("0")
    private Integer coin;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
}
