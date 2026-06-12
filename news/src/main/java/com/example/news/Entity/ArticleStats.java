package com.example.news.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "article_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleStats {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", unique = true)
    private Article article;

    @Column(name = "likes_count")
    private Integer likesCount = 0;

    @Column(name = "shares_count")
    private Integer sharesCount = 0;

    @Column(name = "bookmarks_count")
    private Integer bookmarksCount = 0;

    @Column(name = "comments_count")
    private Integer commentsCount = 0;


}
