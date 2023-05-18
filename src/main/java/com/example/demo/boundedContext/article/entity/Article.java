package com.example.demo.boundedContext.article.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {

    @ManyToOne
    private Member author;
    private String subject;
    private String content;

    public static Article createArticle(Member author, String subject, String content) {
        Article article = new Article();
        article.setAuthor(author);
        article.setSubject(subject);
        article.setContent(content);

        return article;
    }

    public void modifyArticle(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

}
