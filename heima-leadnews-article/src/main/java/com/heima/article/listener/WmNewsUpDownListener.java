package com.heima.article.listener;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.message.WmNewsUpDownConstants;
import com.heima.common.util.JsonUtils;
import com.heima.model.media.dtos.WmNewsDownOrUpDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class WmNewsUpDownListener {
    @Autowired
    private ApArticleService apArticleService;

    @KafkaListener(topics = WmNewsUpDownConstants.WM_NEWS_UP_OR_DOWN_TOPIC)
    public void receiveMsg(ConsumerRecord<String,String> record){
        Optional<ConsumerRecord<String,String>> optional
                = Optional.ofNullable(record);
        if (optional.isPresent()){
            String value = record.value();
            log.info("article服务接收自媒体文章上下架消息,value={}",value);
            WmNewsDownOrUpDto wmNewsDownOrUpDto = JsonUtils.toBean(value, WmNewsDownOrUpDto.class);
            apArticleService.upOrDownArticle(wmNewsDownOrUpDto);
            log.info("article服务自媒体文章上下架成功}");
        }
    }
}
