package com.heima.model.behavior.dtos;

import lombok.Data;

@Data
public class UnLikesBehaviorDto {
    /**
     * 设备ID
     */
    String equipmentId;
    /**
     * 文章ID
     */
    Long articleId;

    /**
     * 不喜欢操作方式
     * 0 不喜欢
     * 1 取消不喜欢
     */
    Integer type;

    /**
     * 定义不喜欢操作的类型
     */
    public enum Type {
        UNLIKE(0), CANCEL(1);
        Integer code;

        Type(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return this.code;
        }
    }
}