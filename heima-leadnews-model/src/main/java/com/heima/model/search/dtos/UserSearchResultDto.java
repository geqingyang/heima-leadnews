package com.heima.model.search.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP用户搜索信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class UserSearchResultDto implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer entryId;

    /**
     * 搜索词
     */
    private String keyword;

    /**
     * 当前状态0 无效 1有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdTime;

}