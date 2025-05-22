package com.project.msy.bookmark.dto;

import com.project.msy.facility.entity.Facility;
import lombok.Data;


@Data
public class BookmarkResponseDTO {
    private Long facilityId;
    private String type;
    private String name;
    private Integer establishedYear;
    private String grade;
    private String facilitySize;
    private String imageUrls;

    public BookmarkResponseDTO(Facility facility) {
        this.facilityId = facility.getId();
        this.type = facility.getType();
        this.name = facility.getName();
        this.establishedYear = facility.getEstablishedYear();
        this.grade = facility.getGrade();
        this.facilitySize = facility.getFacilitySize();
        this.imageUrls = facility.getFacilityImages().isEmpty() ? null :
                facility.getFacilityImages().get(0).getImageUrl();
    }
}
