package com.heima.model.behavior.dtos;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP行为实体表,一个行为实体可能是用户或者设备，或者其它
 * </p>
 *
 * @author itheima
 */
@Data
public class BehaviorEntryDto implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 实体类型
     * 0终端设备
     * 1用户
     */
    private Short type;

    /**
     * 实体ID
     */
    private Integer entryId;

    /**
     * 创建时间
     */
    private Date createdTime;

    public boolean isUser() {
        if (this.getType() != null && this.getType() == Type.USER.getCode()) {
            return true;
        }
        return false;
    }

    public enum Type {
        USER((short) 1), EQUIPMENT((short) 0);
        @Getter
        short code;

        Type(short code) {
            this.code = code;
        }
    }
}