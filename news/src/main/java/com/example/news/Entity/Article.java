package com.example.news.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "short_description", nullable = false, columnDefinition = "TEXT")
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToOne(mappedBy = "article", fetch = FetchType.LAZY)
    private ArticleStats stats;

    @Column(name = "read_time_minutes", nullable = false)
    private Integer readTimeMinutes;

    @Column(name = "is_trending", nullable = false)
    private Boolean isTrending = false;

    @Column(name = "is_breaking", nullable = false)
    private Boolean isBreaking = false;

    @Column(name = "is_live", nullable = false)
    private Boolean isLive = false;

    @Column(name = "is_editors_pick", nullable = false)
    private Boolean isEditorsPick = false;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
