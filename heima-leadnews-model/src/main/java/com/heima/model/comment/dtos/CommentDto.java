package com.heima.model.comment.dtos;

import com.heima.common.dtos.PageRequestDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDto extends PageRequestDto {

    /**
     * 文章id
     */
    private Long articleId;


    // 最小点赞数
    private Long minLikes;

}