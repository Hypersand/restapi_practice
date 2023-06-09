package com.example.demo.boundedContext.article.controller;


import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.article.dto.*;
import com.example.demo.boundedContext.article.entity.Article;
import com.example.demo.boundedContext.article.service.ArticleService;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/articles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1ArticlesController", description = "게시물 CRUD 컨트롤러")
public class ApiV1ArticleController {
    private final ArticleService articleService;
    private final MemberService memberService;


    @GetMapping(value = "")
    @Operation(summary = "조회")
    public RsData<ArticlesResponse> articles() {
        List<Article> articles = articleService.findAll();

        return RsData.of("S-1", "성공", new ArticlesResponse(articles));
    }


    @GetMapping(value = "/{id}")
    @Operation(summary = "단건 조회")
    public RsData<ArticleResponse> article(@PathVariable Long id) {
        Article article = articleService.findById(id).orElseGet(() -> null);

        if (article == null) {
            return RsData.of("F-1",
                    "%d번 게시물은 존재하지 않습니다.".formatted(id),
                    null);
        }


        return RsData.of("S-1", "성공", new ArticleResponse(article));
    }

    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "등록", security = @SecurityRequirement(name = "bearerAuth"))
    public RsData<WriteResponse> write(@AuthenticationPrincipal User user, @Valid @RequestBody WriteRequest writeRequest) {
        Member member = memberService.findByUsername(user.getUsername()).orElseThrow();
        RsData<Article> writeRsData = articleService.write(member, writeRequest.getSubject(), writeRequest.getContent());

        if (writeRsData.isFail()) {
            return (RsData)writeRsData;
        }

        return RsData.of(writeRsData.getResultCode(), writeRsData.getMsg(), new WriteResponse(writeRsData.getData()));
    }


    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "수정", security = @SecurityRequirement(name = "bearerAuth"))
    public RsData<ModifyResponse> modify(@AuthenticationPrincipal User user, @Valid @RequestBody ModifyRequest modifyRequest
        , @PathVariable Long id) {

        Member member = memberService.findByUsername(user.getUsername()).orElseThrow();

        Optional<Article> opArticle = articleService.findById(id);

        if (opArticle.isEmpty()) {
            return RsData.of(
                    "F-1",
                    "%d번 게시물은 존재하지 않습니다.".formatted(id),
                    null);
        }

        Article article = opArticle.get();

        RsData canModifyRs = articleService.canModify(member, article);

        if (canModifyRs.isFail()) {
            return canModifyRs;
        }

        RsData<Article> modifyRs = articleService.modify(article, modifyRequest.getSubject(), modifyRequest.getContent());

        return RsData.of(
                modifyRs.getResultCode(),
                modifyRs.getMsg(),
                new ModifyResponse(modifyRs.getData())
        );
    }


}