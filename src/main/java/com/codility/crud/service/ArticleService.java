package com.codility.crud.service;

import com.codility.crud.domain.Article;
import com.codility.crud.domain.Tag;
import com.codility.crud.dto.ArticaleDTO;
import com.codility.crud.repository.ArticleRepository;
import com.codility.crud.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArticleService {

    @Value("${articles.blacklist}")
    private List<String> blacklist;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    public Optional<ArticaleDTO> findById(Long id) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }

        ArticaleDTO articaleDTO = new ArticaleDTO();
        articaleDTO.setTitle(article.getTitle());
        articaleDTO.setContent(article.getContent());

        List<String> tags = new ArrayList<>();
        for (Tag tag : article.getTags()) {
            tags.add(tag.getTag());
        }
        articaleDTO.setTags(tags);
        return Optional.ofNullable(articaleDTO);
    }

    public List<ArticaleDTO> findByTitle(String title) {

        List<Article> list = articleRepository.findByTitle(title);

        if (list == null || list.isEmpty()) {
            return null;
        }

        List<ArticaleDTO> dtoList = new ArrayList<>();
        for (Article article : list) {
            ArticaleDTO articaleDTO = new ArticaleDTO();
            articaleDTO.setTitle(article.getTitle());
            articaleDTO.setContent(article.getContent());

            List<String> tags = new ArrayList<>();
            for (Tag tag : article.getTags()) {
                tags.add(tag.getTag());
            }
            articaleDTO.setTags(tags);
            dtoList.add(articaleDTO);
        }
        return dtoList;
    }

    public Long create(ArticaleDTO articaleDTO) {

        if(blacklist.stream().filter(articaleDTO.getContent()::contains).findAny().isPresent()){
            throw new RuntimeException("found blacklist word.");
        }

        Article article = new Article();
        article.setTitle(articaleDTO.getTitle());
        article.setContent(articaleDTO.getContent());

        List<Tag> tagList = new ArrayList<>();
        for (String tagString : articaleDTO.getTags()) {
            Tag tag = new Tag();
            tag.setArticle(article);
            tag.setTag(tagString);
            tagList.add(tag);
        }
        article.setTags(new HashSet<>(tagList));
        articleRepository.save(article);

        return article.getId();
    }

    public void update(Long id, ArticaleDTO articaleDTO) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }

        if(blacklist.stream().filter(articaleDTO.getContent()::contains).findAny().isPresent()){
            throw new RuntimeException("found blacklist word.");
        }

        article.setTitle(articaleDTO.getTitle());
        article.setContent(articaleDTO.getContent());

        article.getTags().clear();
        if (articaleDTO.getTags() != null) {
            for (String tagString : articaleDTO.getTags()) {
                Tag tag = new Tag();
                tag.setArticle(article);
                tag.setTag(tagString);
                article.getTags().add(tag);
            }
        }
        articleRepository.save(article);
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new RuntimeException("Article not  found");
        }
        articleRepository.delete(article);
    }

}
