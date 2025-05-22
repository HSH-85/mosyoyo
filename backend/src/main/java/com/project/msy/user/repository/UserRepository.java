package com.project.msy.user.repository;

import com.project.msy.admin.dashboard.dto.UserDailySignupDto;
import com.project.msy.admin.dashboard.dto.UserGrowthDto;
import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId); // 로그인용 ID로 조회

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    // ▶ 수정된 시그니처
    Optional<User> findByEmail(String email);

    Optional<User> findByOauthIdAndProvider(String oauthId, Provider provider);

    Optional<User> findByOauthId(String userId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.project.msy.admin.dashboard.dto.UserGrowthDto(" +
            "CONCAT(CAST(YEAR(u.createdAt) AS string), '-', " +
            "LPAD(CAST(MONTH(u.createdAt) AS string), 2, '0')), " +
            "COUNT(u)) " +
            "FROM User u " +
            "WHERE YEAR(u.createdAt) = :year " +
            "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
            "ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<UserGrowthDto> findMonthlySignups(@Param("year") int year);

    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m-%d') AS date, COUNT(*) AS count, " +
            "(SELECT COUNT(*) FROM user u2 WHERE u2.created_at >= CURDATE() - INTERVAL 30 DAY) AS total " +
            "FROM user u " +
            "WHERE u.created_at >= CURDATE() - INTERVAL 30 DAY " +
            "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m-%d') " +
            "ORDER BY DATE_FORMAT(u.created_at, '%Y-%m-%d')", nativeQuery = true)
    List<UserDailySignupDto> findDailySignupsForLast30Days();
}
