package com.heima.article.v1.controller;

import com.heima.article.service.ApArticleService;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ApArticleService articleService;


    /**
     * 自媒体文章审核通过以后，feign调用，创建article
     * */
    @PostMapping("/api/v1/article/save")
    public Long createArticle(@RequestBody WmNewsResultDTO dto){
       return articleService.createArticle(dto);
    }

    @PostMapping("/api/v1/article/load")
    public ResponseResult<List<ArticleDto>> load(@RequestBody ArticleHomeDto dto) {
        List<ArticleDto> articleDtoList = articleService.load(dto,1);
        return ResponseResult.ok(articleDtoList);
    }
    /**
     * 加载更多
     * @return
     */
    @PostMapping("/api/v1/article/loadmore")
    public ResponseResult<List<ArticleDto>> loadMore(@RequestBody ArticleHomeDto dto) {
        List<ArticleDto> articleDtoList =  articleService.load(dto,1);
        return ResponseResult.ok(articleDtoList);
    }
    /**
     * 加载最新
     * @return
     */
    @PostMapping("/api/v1/article/loadnew")
    public ResponseResult<List<ArticleDto>> loadNew(@RequestBody ArticleHomeDto dto) {
        List<ArticleDto> articleDtoList =   articleService.load(dto,2);
        return ResponseResult.ok(articleDtoList);
    }

    /**
     * 分页查询文章信息
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/api/v1/article/findByPage")
    List<ArticleDto> findArticleByPage(@RequestParam(name = "page") int page,
                                       @RequestParam(name = "size") int size){
        return articleService.findArticleByPage(page,size);
    }

    @GetMapping("/api/v1/article/findById/{id}")
    ArticleDto findById(@RequestParam("id") Long articleId){
        return articleService.findById(articleId);
    }

}
