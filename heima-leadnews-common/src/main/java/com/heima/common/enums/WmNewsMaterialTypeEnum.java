package com.heima.common.enums;

public enum WmNewsMaterialTypeEnum {
    //这个 0 是无图  1 是单图  3 是多图
    NO_IMAGES(0),
    SINGER_IMAGES(1),
    PLURAL_IMAGES(3);
    int type;
    WmNewsMaterialTypeEnum(int type){
        this.type = type;
    }
    public int getType(){
        return type;
    }

}
