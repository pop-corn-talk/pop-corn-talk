package com.popcorntalk.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long recipientId;

    @Column(nullable = false, length = 50)
    private String sender;

    @Column(nullable = false, length = 500)
    private String contents;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Notification(Long recipientId, String sender, String contents) {
        this.recipientId = recipientId;
        this.sender = sender;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
    }

    public static Notification createOf(Long recipientId, String sender, String contents) {
        return new Notification(recipientId, sender, contents);
    }
}
