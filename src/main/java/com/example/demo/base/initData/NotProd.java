package com.example.demo.base.initData;

import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
        @Bean
        CommandLineRunner initData(MemberService memberService, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("1234");

        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member member1 = memberService.join("user1", password, "user1@naver.com");
                Member member2 = memberService.join("user2", password, "user2@gmail.com");
            }
        };
    }
}



