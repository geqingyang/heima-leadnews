package com.heima.model.search.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Data
public class UserSearchDto {

    /**
     * 设备ID
     */
    private String equipmentId;
    /**
     * 搜索关键字
     */
    @NotBlank(message = "搜索关键词不能为空")
    private String searchWords;
    /**
     * 当前页
     */
    private int pageNum;
    /**
     * 分页条数
     */
    private int pageSize;
    /**
     * 最小时间
     */
    private Date minBehotTime;
    /**
     * 接收搜索历史记录id
     */
    Integer id;

    private List<UserSearchResultDto> hisList;

    public int getFromIndex() {
        if (this.pageNum < 1) {
            return 0;
        }
        if (this.pageSize < 1) {
            this.pageSize = 10;
        }
        return this.pageSize * (pageNum - 1);
    }
}