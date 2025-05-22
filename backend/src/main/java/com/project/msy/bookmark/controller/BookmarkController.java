package com.project.msy.bookmark.controller;

import com.project.msy.auth.security.CustomUserDetails;
import com.project.msy.bookmark.dto.BookmarkResponseDTO;
import com.project.msy.bookmark.service.BookmarkService;
import com.project.msy.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // 찜 추가
    @PostMapping("/{facilityId}")
    public ResponseEntity<Void> addBookmark(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long facilityId) {
        User user = userDetails.getUser();
        bookmarkService.addBookmark(user, facilityId);
        return ResponseEntity.ok().build();
    }

    // 찜 취소
    @DeleteMapping("/{facilityId}")
    public ResponseEntity<Void> removeBookmark(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable Long facilityId) {
        bookmarkService.removeBookmark(userDetails.getUser(), facilityId);
        return ResponseEntity.noContent().build();
    }

    // 찜 목록 조회
    @GetMapping
    public ResponseEntity<List<BookmarkResponseDTO>> getBookmarks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(bookmarkService.getBookmarks(userDetails.getUser()));
    }

    //    찜 전체 삭제
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> removeAllBookmark(@AuthenticationPrincipal CustomUserDetails userDetails) {
        bookmarkService.removeAllBookmark(userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
