package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.entity.PostRecode;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRecodeRepository extends JpaRepository<PostRecode, Long> {

    @Query(value = "SELECT Count(*) FROM PostRecode pr WHERE pr.createdAt BETWEEN :todayStart AND :todayEnd AND pr.userId = :userId")
    int getCreatePostCountInToday(Long userId, LocalDateTime todayStart, LocalDateTime todayEnd);

    @Query(value = "SELECT pr FROM PostRecode pr WHERE pr.createdAt < :now")
    List<PostRecode> findOlderThanSevenDaysPosts(LocalDateTime now);
}
