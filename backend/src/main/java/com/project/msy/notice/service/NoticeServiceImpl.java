package com.project.msy.notice.service;

import com.project.msy.notice.dto.NoticeDto;
import com.project.msy.notice.entity.Notice;
import com.project.msy.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeDto createNotice(NoticeDto dto) {
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .isVisible(dto.isVisible())
                .views(0)
                .build();

        Notice saved = noticeRepository.save(notice);
        return toDto(saved);
    }

    @Override
    public Page<NoticeDto> getAllNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Override
    public NoticeDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 공지사항을 찾을 수 없습니다."));
        return toDto(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    // ✅ 조회수만 증가
    @Override
    public void increaseViewCount(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 공지사항을 찾을 수 없습니다."));
        notice.setViews(notice.getViews() + 1);
        noticeRepository.save(notice);
    }

    private NoticeDto toDto(Notice entity) {
        return NoticeDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .isVisible(entity.isVisible())
                .views(entity.getViews())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
