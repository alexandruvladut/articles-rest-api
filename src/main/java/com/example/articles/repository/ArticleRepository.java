package com.example.articles.repository;

import com.example.articles.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
