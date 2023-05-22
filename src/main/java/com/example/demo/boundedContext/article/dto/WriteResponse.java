package com.example.demo.boundedContext.article.dto;

import com.example.demo.boundedContext.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WriteResponse {
    private final Article article;
}