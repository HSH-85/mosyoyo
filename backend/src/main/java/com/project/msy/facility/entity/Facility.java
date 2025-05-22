package com.project.msy.facility.entity;

import com.project.msy.bookmark.entity.Bookmark;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 시설 기본 정보 Entity
 */
@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 255)
    private String name;

    //    설립년도
    @Column(name = "established_year", nullable = false)
    private Integer establishedYear;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 500)
    private String homepage;

    //    병원등급
    @Column(nullable = false)
    private String grade;

    //    상세설명(마크다운으로 할 예정)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "weekday_hours")
    private String weekdayHours;

    @Column(name = "weekend_hours")
    private String weekendHours;

    @Column(name = "holiday_hours")
    private String holidayHours;

    @Column(name = "visiting_hours")
    private String visitingHours;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //    규모 컬럼
    @Column(name = "facility_size")
    private String facilitySize;


    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.viewCount == null) this.viewCount = 0;
        if (this.likeCount == null) this.likeCount = 0;
    }

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FacilityImage> facilityImages = new ArrayList<>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks = new ArrayList<>();


}
