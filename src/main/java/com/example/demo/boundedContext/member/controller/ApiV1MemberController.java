package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.member.dto.LoginRequest;
import com.example.demo.boundedContext.member.dto.LoginResponse;
import com.example.demo.boundedContext.member.dto.MeResponse;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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


    @PostMapping("/login")
    @Operation(summary = "로그인 및 엑세스 토큰 발급")
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        Member member = memberService.findByUsername(loginRequest.getUsername()).orElse(null);

        if (member == null) {
            return RsData.of("F-1", "존재하지 않는 회원입니다.");
        }

        RsData rsData = memberService.canGenAccessToken(member, loginRequest.getPassword());

        if (rsData.isFail()) {
            return rsData;
        }

        String accessToken = memberService.genAccessToken(member);

        return RsData.of("S-1", "엑세스토큰이 발급되었습니다.", new LoginResponse(accessToken));
    }


    @GetMapping(value = "/me", consumes = ALL_VALUE)
    @Operation(summary = "로그인된 사용자의 정보", security = @SecurityRequirement(name = "bearerAuth"))
    public RsData<MeResponse> me(@AuthenticationPrincipal User user) {
        Member user1 = memberService.findByUsername(user.getUsername()).get();

        return RsData.of("S-1", "성공", new MeResponse(user1));
    }

}
