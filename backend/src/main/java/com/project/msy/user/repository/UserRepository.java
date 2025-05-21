package com.project.msy.user.repository;

import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId); // 로그인용 ID로 조회

    boolean existsByUserId(String userId);

    // ▶ 수정된 시그니처
    Optional<User> findByEmail(String email);

    Optional<User> findByOauthIdAndProvider(String oauthId, Provider provider);
}
