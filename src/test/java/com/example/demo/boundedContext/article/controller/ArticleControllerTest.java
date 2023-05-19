package com.example.demo.boundedContext.article.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("게시글 목록 표시")
    void t1() throws Exception{

        //when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/articles")
                )
                .andDo(print());

        //then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles[0].id").exists());
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void t2() throws Exception {

        //when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/articles/1")
                )
                .andDo(print());

        //then

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(1));
    }

    @Test
    @DisplayName("게시글 작성")
    @WithUserDetails("user1")
    void t3() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/v1/articles")
                        .content("""
                                {
                                "subject" : "제목123",
                                "content" : "내용123"
                                }
                                                                        
                                """
                        )
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("게시물이 생성되었습니다."))
                .andExpect(jsonPath("$.data.article.content").value("내용123"))
                .andExpect(jsonPath("$.data.article.subject").value("제목123"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    @WithUserDetails("admin")
    void t4() throws Exception{

        ResultActions resultActions = mvc.perform(
                patch("/api/v1/articles/3")
                        .content("""
                                {
                                "subject" : "제목수정123",
                                "content" : "내용수정123"
                                }
                                """)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());


        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("3번 게시물이 수정되었습니다."))
                .andExpect(jsonPath("$.data.article.subject").value("제목수정123"))
                .andExpect(jsonPath("$.data.article.content").value("내용수정123"));

    }
}