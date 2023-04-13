package com.heima.common.dtos;

import lombok.Data;

import java.util.List;

/**
 * 分页返回对象
 * @param <T>
 */
@Data
public class PageResult<T> extends ResponseResult{

    private Long currentPage;
    private Long size;
    private Long total;
    private List<T> data;

    public PageResult(Long currentPage, Long size, Long total, List<T> data)
    {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
        this.data = data;
    }

}
