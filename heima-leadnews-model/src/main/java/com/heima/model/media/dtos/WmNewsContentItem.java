package com.heima.model.media.dtos;

import com.heima.common.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmNewsContentItem {

    private String type;
    private String value;
    private Integer id;

    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_TEXT = "text";

    public static List<WmNewsContentItem> parseContent(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        return JsonUtils.toList(content, WmNewsContentItem.class);
    }

    public static List<WmNewsContentItem> getImageItems(String content){
        List<WmNewsContentItem>  totalItems = parseContent(content);
        if(CollectionUtils.isEmpty(totalItems)){
            return null;
        }
        return totalItems.stream().filter(WmNewsContentItem::isImageItem).collect(Collectors.toList());
    }

    public static List<WmNewsContentItem> getTextItems(String content){
        List<WmNewsContentItem>  totalItems = parseContent(content);
        if(CollectionUtils.isEmpty(totalItems)){
            return null;
        }
        return totalItems.stream().filter(WmNewsContentItem::isTextItem).collect(Collectors.toList());
    }


    public static boolean isImageItem(WmNewsContentItem item){
        return item.getType().equals(TYPE_IMAGE);
    }

    public static boolean isTextItem(WmNewsContentItem item){
        return item.getType().equals(TYPE_TEXT);
    }



}
