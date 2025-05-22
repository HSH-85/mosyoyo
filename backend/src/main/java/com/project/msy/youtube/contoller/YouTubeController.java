package com.project.msy.youtube.contoller;

import com.project.msy.youtube.dto.VideoDto;
import com.project.msy.youtube.service.YouTubeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class YouTubeController {
    private final YouTubeService svc;
    public YouTubeController(YouTubeService svc) { this.svc = svc; }

    @GetMapping("/api/videos")
    public Mono<List<VideoDto>> getVideos() {
        return svc.fetchPlaylistVideos();
    }
}