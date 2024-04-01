package com.popcorntalk.domain.point.repository;

import com.popcorntalk.domain.point.entity.Point;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByUserId(Long id);

}
