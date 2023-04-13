package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.article.entity.ApAuthor;
import com.heima.model.article.dtos.AuthorDto;

/**
 * <p>
 * APP文章作者信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
public interface ApAuthorService extends IService<ApAuthor> {

    Integer createAuthor(AuthorDto dto);
}
