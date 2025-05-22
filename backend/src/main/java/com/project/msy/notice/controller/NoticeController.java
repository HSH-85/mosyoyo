package com.project.msy.notice.controller;

import com.project.msy.notice.dto.NoticeDto;
import com.project.msy.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 등록
    @PostMapping
    public ResponseEntity<NoticeDto> createNotice(@RequestBody NoticeDto noticeDto) {
        NoticeDto created = noticeService.createNotice(noticeDto);
        return ResponseEntity.ok(created);
    }

    // ✅ 페이지네이션 지원 전체 공지사항 조회
    @GetMapping
    public ResponseEntity<Page<NoticeDto>> getAllNotices(Pageable pageable) {
        Page<NoticeDto> notices = noticeService.getAllNotices(pageable);
        return ResponseEntity.ok(notices);
    }

    // 단일 공지사항 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> getNotice(@PathVariable Long id) {
        NoticeDto notice = noticeService.getNoticeById(id);
        return ResponseEntity.ok(notice);
    }

    // ✅ 조회수 증가 엔드포인트 추가
    @PatchMapping("/{id}/view")
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long id) {
        noticeService.increaseViewCount(id);
        return ResponseEntity.ok().build();
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}
