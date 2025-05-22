package com.project.msy.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FacilityCreateRequestDTO {
    private String type;
    private String name;
    private Integer establishedYear;
    private String address;
    private String phone;
    private String homepage;
    private String grade;
    private String description;
    private String weekdayHours;
    private String weekendHours;
    private String holidayHours;
    private String visitingHours;
    private String facilitySize;
    private List<String> imageUrls;
}
