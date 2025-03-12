package com.example.articles.service;

import com.example.articles.model.Article;
import com.example.articles.repository.ArticleRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getLogger(DataSeeder.class.getName());

    @Autowired
    private ArticleRepository articleRepository;

    private final Faker faker = new Faker();

    @Override
    public void run(String... args) {
        LOGGER.info("DataSeeder is being executed...");

        List<Article> articles = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            Article article = new Article(
                    faker.book().title(),
                    faker.lorem().paragraph(5),
                    faker.name().fullName()
            );
            articles.add(article);
        }

        articleRepository.saveAll(articles);
        LOGGER.info("100 Random Articles Inserted into the Database!");
    }
}
