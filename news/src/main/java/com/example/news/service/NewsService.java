package com.example.news.service;


import com.example.news.Entity.Article;
import com.example.news.Repository.ArticleRepository;
import com.example.news.dto.*;
import com.example.news.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final ArticleRepository articleRepository;



    public FeaturedArticleDto getFeatured() {
        Article article = articleRepository.findFeaturedArticle()
                .orElseThrow(() -> new NotFoundException("Featured article not found"));


        FeaturedArticleDto dto = new FeaturedArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setShortDescription(article.getShortDescription());
        dto.setImageUrl(article.getImageUrl());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setReadTimeMinutes(article.getReadTimeMinutes());
        dto.setIsTrending(article.getIsTrending());

        // 3. маппим category
        FeaturedArticleDto.CategoryDto categoryDto = new FeaturedArticleDto.CategoryDto();
        categoryDto.setId(article.getCategory().getId());
        categoryDto.setName(article.getCategory().getName());
        categoryDto.setSlug(article.getCategory().getSlug());
        categoryDto.setBadgeColor(article.getCategory().getBadgeColor());
        dto.setCategory(categoryDto);

        // 4. маппим author
        FeaturedArticleDto.AuthorDto authorDto = new FeaturedArticleDto.AuthorDto();
        authorDto.setId(article.getAuthor().getId());
        authorDto.setName(article.getAuthor().getName());
        dto.setAuthor(authorDto);

        // 5. маппим stats
        FeaturedArticleDto.StatsDto statsDto = new FeaturedArticleDto.StatsDto();
        statsDto.setLikesCount(article.getStats().getLikesCount());
        statsDto.setSharesCount(article.getStats().getSharesCount());
        statsDto.setBookmarksCount(article.getStats().getBookmarksCount());
        dto.setStats(statsDto);

        return dto;
    }

    public List<BreakingNewsDto> getBreaking() {
        List<Article> articles = articleRepository.findAllByIsBreakingTrueOrderByCreatedAtDesc();


        return articles.stream()
                .map(article -> {
                    BreakingNewsDto dto = new BreakingNewsDto();
                    dto.setId(article.getId());
                    dto.setTitle(article.getTitle());
                    return dto;
                })
                .toList();
    }

    public LiveNewsDto getLive() {
        Article article = articleRepository.findLiveArticle()
                .orElseThrow(() -> new NotFoundException("Live article not found"));


        LiveNewsDto dto = new LiveNewsDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setImageUrl(article.getImageUrl());

        LiveNewsDto.CategoryDto categoryDto = new LiveNewsDto.CategoryDto();
        categoryDto.setName(article.getCategory().getName());
        dto.setCategory(categoryDto);

        return dto;
    };


    public List<EditorsPickDto> getEditorsPicks() {
        List<Article> articles = articleRepository.findEditorsPicks();

        return articles.stream()
                .map(article -> {
                    EditorsPickDto dto = new EditorsPickDto();
                    dto.setId(article.getId());
                    dto.setTitle(article.getTitle());
                    dto.setImageUrl(article.getImageUrl());

                    EditorsPickDto.CategoryDto categoryDto = new EditorsPickDto.CategoryDto();
                    categoryDto.setId(article.getCategory().getId());
                    categoryDto.setName(article.getCategory().getName());
                    categoryDto.setBadgeColor(article.getCategory().getBadgeColor());
                    dto.setCategory(categoryDto);

                    EditorsPickDto.StatsDto statsDto = new EditorsPickDto.StatsDto();
                    statsDto.setLikesCount(article.getStats().getLikesCount());
                    statsDto.setSharesCount(article.getStats().getSharesCount());
                    statsDto.setBookmarksCount(article.getStats().getBookmarksCount());
                    dto.setStats(statsDto);

                    return dto;
                })
                .toList();
    }


    public Map<String, Object> getArticles(String tab, int page, int limit) {

        // 1. создаём Pageable (page-1 потому что Spring считает с 0, фронтенд с 1)
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());

        // 2. tab → slug (latest остаётся latest, остальные как есть)
        String slug = (tab == null || tab.isEmpty()) ? "latest" : tab;

        // 3. достаём из БД
        Page<Article> articlePage = articleRepository.findArticles(slug, pageable);

        // 4. маппим Article → ArticleDto
        List<ArticleDto> articles = articlePage.getContent().stream()
                .map(article -> {
                    ArticleDto dto = new ArticleDto();
                    dto.setId(article.getId());
                    dto.setTitle(article.getTitle());
                    dto.setShortDescription(article.getShortDescription());
                    dto.setImageUrl(article.getImageUrl());
                    dto.setCreatedAt(article.getCreatedAt());
                    dto.setReadTimeMinutes(article.getReadTimeMinutes());

                    ArticleDto.CategoryDto categoryDto = new ArticleDto.CategoryDto();
                    categoryDto.setId(article.getCategory().getId());
                    categoryDto.setName(article.getCategory().getName());
                    categoryDto.setSlug(article.getCategory().getSlug());
                    categoryDto.setBadgeColor(article.getCategory().getBadgeColor());
                    dto.setCategory(categoryDto);

                    ArticleDto.AuthorDto authorDto = new ArticleDto.AuthorDto();
                    authorDto.setId(article.getAuthor().getId());
                    authorDto.setName(article.getAuthor().getName());
                    dto.setAuthor(authorDto);

                    ArticleDto.StatsDto statsDto = new ArticleDto.StatsDto();
                    statsDto.setLikesCount(article.getStats().getLikesCount());
                    statsDto.setSharesCount(article.getStats().getSharesCount());
                    statsDto.setBookmarksCount(article.getStats().getBookmarksCount());
                    dto.setStats(statsDto);

                    return dto;
                })
                .toList();

        // 5. собираем response с pagination
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("articles", articles);
        response.put("pagination", Map.of(
                "current_page", page,
                "total_pages", articlePage.getTotalPages(),
                "total_items", articlePage.getTotalElements(),
                "limit", limit
        ));

        return response;
    }


    public ArticleDetailDto getArticleById(Integer id) {
        Article article = articleRepository.findArticleById(id)
                .orElseThrow(() -> new NotFoundException("Article not found with id: " + id));

        ArticleDetailDto dto = new ArticleDetailDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setShortDescription(article.getShortDescription());
        dto.setContent(article.getContent());
        dto.setImageUrl(article.getImageUrl());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setReadTimeMinutes(article.getReadTimeMinutes());
        dto.setIsTrending(article.getIsTrending());
        dto.setIsBreaking(article.getIsBreaking());
        dto.setIsLive(article.getIsLive());

        ArticleDetailDto.CategoryDto categoryDto = new ArticleDetailDto.CategoryDto();
        categoryDto.setId(article.getCategory().getId());
        categoryDto.setName(article.getCategory().getName());
        categoryDto.setSlug(article.getCategory().getSlug());
        categoryDto.setBadgeColor(article.getCategory().getBadgeColor());
        dto.setCategory(categoryDto);

        ArticleDetailDto.AuthorDto authorDto = new ArticleDetailDto.AuthorDto();
        authorDto.setId(article.getAuthor().getId());
        authorDto.setName(article.getAuthor().getName());
        authorDto.setAvatarUrl(article.getAuthor().getAvatarUrl());
        authorDto.setBio(article.getAuthor().getBio());
        authorDto.setTwitterHandle(article.getAuthor().getTwitterHandle());
        dto.setAuthor(authorDto);

        ArticleDetailDto.StatsDto statsDto = new ArticleDetailDto.StatsDto();
        statsDto.setLikesCount(article.getStats().getLikesCount());
        statsDto.setSharesCount(article.getStats().getSharesCount());
        statsDto.setBookmarksCount(article.getStats().getBookmarksCount());
        statsDto.setCommentsCount(article.getStats().getCommentsCount());
        dto.setStats(statsDto);

        return dto;
    }

    public List<RelatedArticleDto> getRelated(Integer id) {
        Article article = articleRepository.findArticleById(id)
                .orElseThrow(() -> new NotFoundException("Article not found with id: " + id));

        List<Article> related = articleRepository.findRelated(article.getCategory().getId(), id);

        return related.stream()
                .map(a -> {
                    RelatedArticleDto dto = new RelatedArticleDto();
                    dto.setId(a.getId());
                    dto.setTitle(a.getTitle());
                    dto.setShortDescription(a.getShortDescription());
                    dto.setImageUrl(a.getImageUrl());

                    RelatedArticleDto.CategoryDto categoryDto = new RelatedArticleDto.CategoryDto();
                    categoryDto.setId(a.getCategory().getId());
                    categoryDto.setName(a.getCategory().getName());
                    categoryDto.setBadgeColor(a.getCategory().getBadgeColor());
                    dto.setCategory(categoryDto);

                    RelatedArticleDto.StatsDto statsDto = new RelatedArticleDto.StatsDto();
                    statsDto.setLikesCount(a.getStats().getLikesCount());
                    statsDto.setSharesCount(a.getStats().getSharesCount());
                    statsDto.setBookmarksCount(a.getStats().getBookmarksCount());
                    dto.setStats(statsDto);

                    return dto;
                })
                .toList();
    }


}
