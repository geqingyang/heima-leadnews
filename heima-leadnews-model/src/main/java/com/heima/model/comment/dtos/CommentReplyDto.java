package com.heima.model.comment.dtos;

import com.heima.common.dtos.PageRequestDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class CommentReplyDto extends PageRequestDto {

    /**
     * 评论id
     */
    private String commentId;

    // 最小时间
    private Long minLikes;
}