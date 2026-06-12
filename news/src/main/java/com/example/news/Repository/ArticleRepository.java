package com.example.news.Repository;

import com.example.news.Entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    @Query("""
    Select a from Article a
    Join Fetch a.category
    Join Fetch a.author
    Join Fetch a.stats
    WHERE a.isFeatured = true
    Order By a.createdAt DESC
    Limit 1
""")

    Optional<Article> findFeaturedArticle();

    List<Article> findAllByIsBreakingTrueOrderByCreatedAtDesc();



    @Query("""
    SELECT a FROM Article a
    JOIN FETCH a.category
    WHERE a.isLive = true
    ORDER BY a.createdAt DESC
    LIMIT 1
""")
    Optional<Article> findLiveArticle();



    @Query("""
    SELECT a FROM Article a
    JOIN FETCH a.category
    JOIN FETCH a.stats
    WHERE a.isEditorsPick = true
""")
    List<Article> findEditorsPicks();


    @Query("""
    SELECT a FROM Article a
    JOIN FETCH a.category
    JOIN FETCH a.author
    JOIN FETCH a.stats
    WHERE (:slug = 'latest' OR a.category.slug = :slug)
    ORDER BY a.createdAt DESC
    """)
    Page<Article> findArticles(@Param("slug") String slug, Pageable pageable);



    @Query("""
    SELECT a FROM Article a
    JOIN FETCH a.category
    JOIN FETCH a.author
    JOIN FETCH a.stats
    WHERE a.id = :id
""")
    Optional<Article> findArticleById(@Param("id") Integer id);


    @Query("""
    SELECT a FROM Article a
    JOIN FETCH a.category
    JOIN FETCH a.stats
    WHERE a.category.id = :categoryId
    AND a.id != :excludeId
    ORDER BY a.createdAt DESC
    LIMIT 5
    """)
    List<Article> findRelated(@Param("categoryId") Integer categoryId,
                              @Param("excludeId") Integer excludeId);




}
