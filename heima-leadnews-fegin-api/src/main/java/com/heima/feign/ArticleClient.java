package com.heima.feign;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.AuthorDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("leadnews-article")
public interface ArticleClient {

    @PostMapping("/api/v1/author/save")
    Integer createAuthor(@RequestBody AuthorDto dto);

    @PostMapping("/api/v1/article/save")
    Long createArticle(@RequestBody WmNewsResultDTO dto);

    /**
     * 分页查询文章信息
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/api/v1/article/findByPage")
    List<ArticleDto> findArticleByPage(@RequestParam(name = "page") int page,
                                       @RequestParam(name = "size") int size);

    @GetMapping("/api/v1/article/findById/{id}")
    ArticleDto findById(@RequestParam("id") Long articleId);
}
