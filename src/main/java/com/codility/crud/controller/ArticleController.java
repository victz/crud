package com.codility.crud.controller;

import com.codility.crud.domain.Article;
import com.codility.crud.dto.ArticaleDTO;
import com.codility.crud.repository.ArticleRepository;
import com.codility.crud.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @PostMapping("/article/create")
    public Long create(@RequestBody ArticaleDTO model) {
        return articleService.create(model);
    }

    @GetMapping("/article/list")
    public List<Article> list() {
        return articleRepository.findAll();
    }

    @GetMapping("/article/list/title")
    public List<ArticaleDTO> list(@RequestBody ArticaleDTO articaleDTO) {
        return articleService.findByTitle(articaleDTO.getTitle());
    }

    @GetMapping("/article/{id}")
    public ArticaleDTO details(@PathVariable String id) {
        return articleService.findById(Long.valueOf(id)).orElse(null);
    }


    @PutMapping("/article/{id}")
    public void update(@PathVariable String id, @RequestBody ArticaleDTO model) {
        articleService.update(Long.valueOf(id), model);
    }

    @DeleteMapping("/article/{id}")
    public void delete(@PathVariable String id) {
        articleService.delete(Long.valueOf(id));
    }
}
