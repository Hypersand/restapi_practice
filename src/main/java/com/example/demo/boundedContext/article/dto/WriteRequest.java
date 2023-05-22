package com.example.demo.boundedContext.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WriteRequest {
    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}