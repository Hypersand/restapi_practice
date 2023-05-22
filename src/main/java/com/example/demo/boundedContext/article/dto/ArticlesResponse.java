package com.example.demo.boundedContext.article.dto;

import com.example.demo.boundedContext.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ArticlesResponse {
    private final List<Article> articles;
}
