package com.example.demo.member.controller;

import com.example.demo.base.rsData.RsData;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/member", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1MemberController", description = "로그인 및 로그인 된 회원의 정보")
public class ApiV1MemberController {

    private final MemberService memberService;

    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @AllArgsConstructor
    @Getter
    public static class LoginResponse {
        private final String accessToken;
    }


    @PostMapping("/login")
    @Operation(summary = "로그인 및 엑세스 토큰 발급")
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getPassword());

        return RsData.of("S-1", "엑세스토큰이 발급되었습니다.", new LoginResponse(accessToken));
    }

    @AllArgsConstructor
    @Getter
    public static class MeResponse {
        private final Member member;
    }

    @GetMapping(value = "/me", consumes = ALL_VALUE)
    @Operation(summary = "로그인 된 회원의 정보")
    public RsData<MeResponse> me(@AuthenticationPrincipal User user) {
        Member user1 = memberService.findByUsername(user.getUsername()).get();

        return RsData.of("S-1", "성공", new MeResponse(user1));
    }

}
