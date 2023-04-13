package com.heima.model.article.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * APP文章作者信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class AuthorDto implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 作者名称
     */
    private String name;

    /**
     * 0 爬取数据
     * 1 签约合作商
     * 2 平台自媒体人
     */
    private Integer type;

    /**
     * 社交账号ID
     */
    private Integer userId;

    /**
     * 自媒体账号
     */
    private Integer wmUserId;

}