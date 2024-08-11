package com.example.jejutravel.domain.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId; // 비식별자 id (자동 증가)

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName; // 사용자 이름

    @Column(name = "user_date_of_birth", nullable = false)
    private Date userDateOfBirth; // 생년월일

    @Column(name = "user_phone_number", nullable = false, length = 15)
    private String userPhoneNumber; // 전화번호

    @Column(name = "user_gender", nullable = false, length = 1)
    private boolean userGender; // 성별

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail; // 이메일

    @Column(name = "user_username", nullable = false, length = 50)
    private String userUsername; // 아이디

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword; // 비밀번호

    @Column(name = "user_created_at", nullable = false)
    private Date userCreatedAt; // 생성일시

    @Column(name = "user_updated_at", nullable = false)
    private Date userUpdatedAt; // 수정일시

    @Builder(toBuilder = true)
    public User(Long userId, String userName, Date userDateOfBirth, String userPhoneNumber,
            boolean userGender, String userEmail, String userUsername, String userPassword,
            Date userCreatedAt, Date userUpdatedAt) {
        this.userId = userId;
        this.userName = userName;
        this.userDateOfBirth = userDateOfBirth;
        this.userPhoneNumber = userPhoneNumber;
        this.userGender = userGender;
        this.userEmail = userEmail;
        this.userUsername = userUsername;
        this.userPassword = userPassword;
        this.userCreatedAt = userCreatedAt != null ? userCreatedAt : Date.valueOf(LocalDate.now());
        this.userUpdatedAt = userUpdatedAt;
    }
}
