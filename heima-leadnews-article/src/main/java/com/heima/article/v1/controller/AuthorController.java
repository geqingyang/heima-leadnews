package com.heima.article.v1.controller;

import com.heima.article.service.ApAuthorService;
import com.heima.model.article.dtos.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {

    @Autowired
    private ApAuthorService authorService;

    /**
     * 前端app用户审核通过以后，创建文章作者
     *
     */
    //POST /api/v1/author/save
    @PostMapping("/api/v1/author/save")
    public Integer createAuthor(@RequestBody AuthorDto dto){
        return authorService.createAuthor(dto);
    }

}
