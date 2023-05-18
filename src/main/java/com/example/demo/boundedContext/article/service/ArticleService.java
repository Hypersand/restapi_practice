package com.example.demo.boundedContext.article.service;

import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.article.entity.Article;
import com.example.demo.boundedContext.article.repository.ArticleRepository;
import com.example.demo.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public RsData<Article> write(Member author, String subject, String content) {
        Article article = Article.createArticle(author, subject, content);
        articleRepository.save(article);

        return RsData.of("S-1", "게시물이 생성되었습니다.", article);
    }

    public List<Article> findAll() {
        return articleRepository.findAllByOrderByIdDesc();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public RsData canDelete(Member actor, Article article) {
        if (Objects.equals(actor.getId(), article.getAuthor().getId())) {
            return RsData.of("S-1", "게시물을 삭제할 수 있습니다.");
        }

        return RsData.of("F-1", "게시물을 삭제할 수 없습니다.");
    }


    @Transactional
    public RsData<Article> modify(Article article, String subject, String content) {

        article.modifyArticle(subject, content);

        return RsData.of(
                "S-1",
                "%d번 게시물이 수정되었습니다.".formatted(article.getId()),
                article
        );
    }

    public RsData canModify(Member actor, Article article) {
        if (Objects.equals(actor.getId(), article.getAuthor().getId())) {
            return RsData.of("S-1", "게시물을 수정할 수 있습니다.");
        }

        return RsData.of("F-1", "게시물을 수정할 수 없습니다.");
    }
}
