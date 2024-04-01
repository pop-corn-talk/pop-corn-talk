package com.popcorntalk.domain.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int point;

    private Point(Long userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public static Point createOf(Long userId, int point) {
        return new Point(userId, point);
    }

    public void updatePoint(int point) {
        this.point = point;
    }
}
