package com.popcorntalk.domain.point.entity;

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
@Table(name = "point_records")
public class PointRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pointId;

    private int previousPoint;

    private int amount;

    private int finalPoint;

    private LocalDateTime createdAt;

    private PointRecord(Long pointId, int previousPoint, int amount, int finalPoint) {
        this.pointId = pointId;
        this.previousPoint = previousPoint;
        this.amount = amount;
        this.finalPoint = finalPoint;
        this.createdAt = LocalDateTime.now();
    }

    public static PointRecord createOf(Long pointId, int previousPoint, int amount,
        int finalPoint) {
        return new PointRecord(pointId, previousPoint, amount, finalPoint);
    }
}
