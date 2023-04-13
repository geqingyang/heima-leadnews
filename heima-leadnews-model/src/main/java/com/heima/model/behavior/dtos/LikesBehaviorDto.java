package com.heima.model.behavior.dtos;

import lombok.Data;

@Data
public class LikesBehaviorDto {
    /**
     * 设备ID
     */
    String equipmentId;
    /**
     * 文章、动态、评论等ID
     */
    Long articleId;
    /**
     * 喜欢内容类型
     * 0文章
     * 1动态
     * 2评论
     */
    Integer type;

    /**
     * 操作方式
     * 1 点赞
     * 0 取消点赞
     */
    Integer operation;

    // 定义点赞内容的类型
    public enum Type {
        ARTICLE(0), DYNAMIC(1), COMMENT(2);
        Integer code;

        Type(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return this.code;
        }
    }

    //定义点赞操作的方式，点赞还是取消点赞
    public enum Operation {
        LIKE(1), CANCEL(0);
        Integer code;

        Operation(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return this.code;
        }
    }
}