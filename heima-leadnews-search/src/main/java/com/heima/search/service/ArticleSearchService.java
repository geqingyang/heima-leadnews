package com.heima.search.service;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.search.dtos.UserSearchDto;

import java.util.List;

public interface ArticleSearchService {
    void importArticle(List<ArticleDto> articleDtoList);

    List<ArticleDto> search(UserSearchDto dto);

    void onArticleCreate(Long articleId);
}
