package com.example.news.dto;

import lombok.Data;

@Data
public class LiveNewsDto {

    private Integer id;
    private String title;
    private String imageUrl;

    private CategoryDto category;

    @Data
    public static class CategoryDto {
        private String name;
    }

}
