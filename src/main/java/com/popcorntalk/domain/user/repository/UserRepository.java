package com.popcorntalk.domain.user.repository;

import com.popcorntalk.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    //todo 쿼리 dsl 마춰서 복원 작업 해야 합니다
    //boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
