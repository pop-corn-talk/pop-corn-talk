package com.popcorntalk.domain.user.entity;

import com.popcorntalk.global.entity.DeletionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    private static final long SIGNUP_REWARD = 1000L;
    private static final long MAX_DAILY_POSTS_LIMIT = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    private Long point;

    private Long maxDailyPostsLimit;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private DeletionStatus deletionStatus;

    public User(Long userId, String email) {
        this.id = userId;
        this.email = email;
    }

    public static User SignupOf(String email, String password) {
        return User.builder()
            .email(email)
            .password(password)
            .point(SIGNUP_REWARD)
            .maxDailyPostsLimit(MAX_DAILY_POSTS_LIMIT)
            .role(UserRoleEnum.USER)
            .deletionStatus(DeletionStatus.N)
            .build();
    }

    public void SpendDailyPostChanceAndEarnPoints(){
        if(maxDailyPostsLimit == 0)
            // todo Custom Exception 만들기 전까지 임시
            throw new IllegalArgumentException();

        maxDailyPostsLimit--;
        point += 1000;
    }
}