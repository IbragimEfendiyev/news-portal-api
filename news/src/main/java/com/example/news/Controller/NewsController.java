package com.example.news.Controller;


import com.example.news.exception.BadRequestException;
import com.example.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/featured")
    public ResponseEntity<?> getFeatured() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getFeatured()
        ));
    }

    @GetMapping("/breaking")
    public ResponseEntity<?> getBreaking() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getBreaking()
        ));
    }

    @GetMapping("/live")
    public ResponseEntity<?> getLive() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getLive()
        ));
    }

    @GetMapping("/editors-picks")
    public ResponseEntity<?> getEditorsPicks() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getEditorsPicks()
        ));
    }

    @GetMapping("/articles")
    public ResponseEntity<?> getArticles(
            @RequestParam(defaultValue = "latest") String tab,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int limit
    ) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getArticles(tab, page, limit)
        ));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable String id) {
        // валидация — id должен быть числом
        if (!id.matches("\\d+")) {
            throw new BadRequestException("Invalid article id: " + id);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getArticleById(Integer.parseInt(id))
        ));
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<?> getRelated(@PathVariable String id) {
        if (!id.matches("\\d+")) {
            throw new BadRequestException("Invalid article id: " + id);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OK",
                "data", newsService.getRelated(Integer.parseInt(id))
        ));
    }
}
