package com.project.msy.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private String videoId;
    private String title;
    private String thumbnailUrl;
    private String publishedAt;
}