package com.popcorntalk.domain.point.repository;

import com.popcorntalk.domain.point.entity.PointRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRecordRepository extends JpaRepository<PointRecord, Long> {

}
