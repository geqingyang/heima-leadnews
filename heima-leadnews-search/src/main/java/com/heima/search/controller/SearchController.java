package com.heima.search.controller;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.ArticleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private ArticleSearchService searchService;

    /**
     * app用户的搜索
     *
     */
    @PostMapping("/api/v1/article/search/search")
    public ResponseResult<List<ArticleDto>> search(@RequestBody UserSearchDto dto){
        List<ArticleDto> list = searchService.search(dto);
        return ResponseResult.ok(list);
    }
}
