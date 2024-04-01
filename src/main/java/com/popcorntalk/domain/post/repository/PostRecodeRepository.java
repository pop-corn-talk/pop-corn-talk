package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.entity.PostRecode;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRecodeRepository extends JpaRepository<PostRecode, Long> {

    @Query(value = "SELECT Count(*) FROM PostRecode WHERE createdAt BETWEEN :todayStart AND :todayEnd AND userId = :userId")
    Long existsCreatePostInToday(Long userId, LocalDateTime todayStart, LocalDateTime todayEnd);
}
