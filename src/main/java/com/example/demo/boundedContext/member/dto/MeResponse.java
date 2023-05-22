package com.example.demo.boundedContext.member.dto;

import com.example.demo.boundedContext.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MeResponse {
    private final Member member;
}
