package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopularFacilityDto {
    private Long   id;
    private String name;
    private String type;
    private Integer viewCount;
    private Integer likeCount;
    private Integer reviewCount;

}