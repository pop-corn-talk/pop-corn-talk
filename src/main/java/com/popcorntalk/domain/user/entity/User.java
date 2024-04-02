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


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private DeletionStatus deletionStatus;

    public User(Long userId, String email) {
        this.id = userId;
        this.email = email;
    }
    private User(String email,String password,DeletionStatus deletionStatus,UserRoleEnum userRoleEnum){
        this.email = email;
        this.password = password;
        this.deletionStatus = deletionStatus;
        this.role = userRoleEnum;
    }
    public static User createOf(String email,String password,DeletionStatus deletionStatus,UserRoleEnum userRoleEnum){
        return new User(email,password,deletionStatus,userRoleEnum);
    }
}
