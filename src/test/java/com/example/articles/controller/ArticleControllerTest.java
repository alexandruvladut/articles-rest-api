package com.example.articles.controller;

import com.example.articles.model.Article;
import com.example.articles.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

public class ArticleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void createArticle_ShouldReturnCreatedArticle() throws Exception {
        Article article = new Article("Test Title", "Test Content", "John Doe");
        when(articleService.createArticle(any(Article.class))).thenReturn(article);

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Title\", \"content\":\"Test Content\", \"author\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.author").value("John Doe"));

        verify(articleService, times(1)).createArticle(any(Article.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getArticleById_ShouldReturnArticleIfExists() throws Exception {
        Article article = new Article("Test Title", "Test Content", "John Doe");
        when(articleService.getArticleById(1L)).thenReturn(Optional.of(article));

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.author").value("John Doe"));

        verify(articleService, times(1)).getArticleById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getArticleById_ShouldReturnNotFoundIfNotExists() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isNotFound());

        verify(articleService, times(1)).getArticleById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateArticle_ShouldReturnUpdatedArticle() throws Exception {
        Article existingArticle = new Article("Old Title", "Old Content", "John Doe");
        Article updatedArticle = new Article("Updated Title", "Updated Content", "John Doe");

        when(articleService.getArticleById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleService.updateArticle(any(Article.class))).thenReturn(updatedArticle);

        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"content\":\"Updated Content\", \"author\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        verify(articleService, times(1)).getArticleById(1L);
        verify(articleService, times(1)).updateArticle(any(Article.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateArticle_ShouldReturnNotFoundIfArticleNotExists() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"content\":\"Updated Content\", \"author\":\"John Doe\"}"))
                .andExpect(status().isNotFound());

        verify(articleService, times(1)).getArticleById(1L);
        verify(articleService, times(0)).updateArticle(any(Article.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getArticles_ShouldReturnPagedResults() throws Exception {
        List<Article> articles = Arrays.asList(
                new Article("Title 1", "Content 1", "Author 1"),
                new Article("Title 2", "Content 2", "Author 2")
        );

        Page<Article> page = new PageImpl<>(articles, PageRequest.of(0, 10), articles.size());
        when(articleService.getArticlesByCreatedAtDesc(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/articles?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(articleService, times(1)).getArticlesByCreatedAtDesc(0, 10);
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchArticlesByTitle_ShouldReturnFilteredResults() throws Exception {
        List<Article> articles = Arrays.asList(
                new Article("Matching Title", "Content 1", "Author 1")
        );

        Page<Article> page = new PageImpl<>(articles, PageRequest.of(0, 10), articles.size());
        when(articleService.searchByTitle("Matching", 0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/articles/search?title=Matching&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Matching Title"));

        verify(articleService, times(1)).searchByTitle("Matching", 0, 10);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteArticle_ShouldReturnNoContent() throws Exception {
        doNothing().when(articleService).deleteArticle(1L);

        mockMvc.perform(delete("/api/articles/1"))
                .andExpect(status().isNoContent());

        verify(articleService, times(1)).deleteArticle(1L);
    }
}
