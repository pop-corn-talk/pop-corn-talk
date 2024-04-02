package com.popcorntalk.domain.user.repository;

import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> ,UserRepositoryCustom{

    boolean existsByEmail(String email);

    @Override
    User getUser(Long userId);

    @Override
    Page<UserPublicInfoResponseDto> getPageUsers(Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u " + "WHERE u.id = :userId AND u.role = 'ADMIN' AND u.deletionStatus = 'N'")
    boolean validateAdminUser(Long userId);
}
