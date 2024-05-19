package com.codility.crud.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticaleDTO {

    private String title;
    private String content;
    private List<String> tags;
}
