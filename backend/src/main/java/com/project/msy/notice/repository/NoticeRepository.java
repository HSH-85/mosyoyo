package com.project.msy.notice.repository;

import com.project.msy.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // ✅ createdAt 기준 내림차순 정렬 (최신 공지사항이 먼저)
    List<Notice> findAllByOrderByCreatedAtDesc();

}
