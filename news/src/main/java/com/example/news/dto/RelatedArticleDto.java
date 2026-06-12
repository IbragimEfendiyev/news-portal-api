package com.example.news.dto;

import lombok.Data;

@Data
public class RelatedArticleDto {

    private Integer id;
    private String title;
    private String shortDescription;
    private String imageUrl;

    private CategoryDto category;
    private StatsDto stats;

    @Data
    public static class CategoryDto {
        private Integer id;
        private String name;
        private String badgeColor;
    }

    @Data
    public static class StatsDto {
        private Integer likesCount;
        private Integer sharesCount;
        private Integer bookmarksCount;
    }
}