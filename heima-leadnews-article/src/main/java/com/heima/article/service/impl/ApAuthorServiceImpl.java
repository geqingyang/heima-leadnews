package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.entity.ApAuthor;
import com.heima.article.mapper.ApAuthorMapper;
import com.heima.article.service.ApAuthorService;
import com.heima.model.article.dtos.AuthorDto;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP文章作者信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor> implements ApAuthorService {

    @Override
    public Integer createAuthor(AuthorDto dto) {
        //跟据wmUserId查author，存在则更新，不存在则insert
        LambdaQueryWrapper<ApAuthor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApAuthor::getWmUserId, dto.getWmUserId());
        ApAuthor authDB = this.getOne(queryWrapper);
        ApAuthor author = buildAuthor(dto, authDB == null ? null : authDB.getId());
        this.saveOrUpdate(author);
        return author.getId();
    }

    private ApAuthor buildAuthor(AuthorDto dto, Integer authorId){
        ApAuthor author = new ApAuthor();
        author.setId(authorId);
        author.setName(dto.getName());
        author.setType(dto.getType());
        if(authorId == null){
            author.setUserId(dto.getUserId());
            author.setWmUserId(dto.getWmUserId());
            author.setCreatedTime(new Date());
        }
        return author;
    }
}
