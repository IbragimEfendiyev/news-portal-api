package com.example.news.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleDto {

    private Integer id;
    private String title;
    private String shortDescription;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Integer readTimeMinutes;

    private CategoryDto category;
    private AuthorDto author;
    private StatsDto stats;

    @Data
    public static class CategoryDto {
        private Integer id;
        private String name;
        private String slug;
        private String badgeColor;
    }

    @Data
    public static class AuthorDto {
        private Integer id;
        private String name;
    }

    @Data
    public static class StatsDto {
        private Integer likesCount;
        private Integer sharesCount;
        private Integer bookmarksCount;
    }
}