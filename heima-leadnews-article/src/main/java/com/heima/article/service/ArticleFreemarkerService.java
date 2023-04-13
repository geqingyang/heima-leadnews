package com.heima.article.service;

import com.heima.article.entity.ApArticle;

public interface ArticleFreemarkerService {

    /**
     * 生成文章详情静态页面
     * 写入Minio
     * @param content
     */
    void buildContentHtml(ApArticle apArticle, String content);
}
