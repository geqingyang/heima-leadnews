package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.article.entity.ApHotArticles;

/**
 * <p>
 * 热文章表 服务类
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
public interface ApHotArticlesService extends IService<ApHotArticles> {
    void findHotArticle();

}
