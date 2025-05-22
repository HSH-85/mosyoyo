package com.project.msy.youtube.service;

import com.project.msy.youtube.dto.VideoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class YouTubeService {
    private final WebClient client;
    private final String apiKey;
    private final String playlistId;

    public YouTubeService(
            WebClient.Builder builder,
            @Value("${youtube.api.key}") String apiKey,
            @Value("${youtube.api.playlist-id}") String playlistId
    ) {
        this.client = builder.baseUrl("https://www.googleapis.com/youtube/v3").build();
        this.apiKey = apiKey;
        this.playlistId = playlistId;
    }

    public Mono<List<VideoDto>> fetchPlaylistVideos() {
        return fetchPage("")
                .map(page -> {
                    List<VideoDto> acc = new ArrayList<>();
                    recurse(page, acc);
                    return acc;
                });
    }

    private Mono<Map> fetchPage(String token) {
        return client.get()
                .uri(uri -> uri
                        .path("/playlistItems")
                        .queryParam("part", "snippet,contentDetails")
                        .queryParam("playlistId", playlistId)
                        .queryParam("maxResults", 50)
                        .queryParam("pageToken", token)
                        .queryParam("key", apiKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(Map.class);
    }

    @SuppressWarnings("unchecked")
    private void recurse(Map<?,?> page, List<VideoDto> acc) {
        ((List<Map<String,Object>>)page.get("items")).forEach(item -> {
            Map<String,Object> sn = (Map<String,Object>)item.get("snippet");
            Map<String,Object> cd = (Map<String,Object>)item.get("contentDetails");
            String vid = cd.get("videoId").toString();
            String thumb = ((Map<String,Map<String,String>>)sn.get("thumbnails"))
                    .get("medium").get("url");
            acc.add(new VideoDto(
                    vid,
                    sn.get("title").toString(),
                    thumb,
                    sn.get("publishedAt").toString()
            ));
        });
        String next = (String)page.get("nextPageToken");
        if (next != null) {
            fetchPage(next).map(p -> { recurse(p, acc); return p; }).block();
        }
    }
}