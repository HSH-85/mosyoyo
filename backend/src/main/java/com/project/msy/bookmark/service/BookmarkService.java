package com.project.msy.bookmark.service;

import com.project.msy.bookmark.dto.BookmarkResponseDTO;
import com.project.msy.bookmark.entity.Bookmark;
import com.project.msy.bookmark.repository.BookmarkRepository;
import com.project.msy.facility.entity.Facility;
import com.project.msy.facility.repository.FacilityRepository;
import com.project.msy.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final FacilityRepository facilityRepository;

//    찜하기
    @Transactional
    public void addBookmark(User user, Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 시설입니다."));
        boolean alreadyExists = bookmarkRepository.existsByUserAndFacility(user, facility);
        if (alreadyExists) {
            throw new IllegalArgumentException("이미 찜한 사람입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .facility(facility)
                .build();
        bookmarkRepository.save(bookmark);
    }

    //    찜 취소
    @Transactional
    public void removeBookmark(User user, Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 시설이 없습니다."));
        Bookmark bookmark = bookmarkRepository.findByUserAndFacility(user, facility)
                .orElseThrow(() -> new IllegalArgumentException("찜한 기록이 없습니다."));
        bookmarkRepository.delete(bookmark);
    }

    //    찜목록 조회
    @Transactional
    public List<BookmarkResponseDTO> getBookmarks(User user) {
        return bookmarkRepository.findAllByUser(user).stream()
                .map(bookmark -> new BookmarkResponseDTO(bookmark.getFacility()))
                .collect(Collectors.toList());
    }
    //    찜 전체 삭제
    @Transactional
    public void removeAllBookmark(User user) {
        bookmarkRepository.deleteAllByUser(user);
    }


}
