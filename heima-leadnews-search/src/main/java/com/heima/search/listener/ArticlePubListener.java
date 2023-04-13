package com.heima.search.listener;

import com.heima.common.constants.message.Article2EsConstants;

import com.heima.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ArticlePubListener {

    @Autowired
    private ArticleSearchService searchService;

    @KafkaListener(topics = Article2EsConstants.ARTICLE_2_ES_TOPIC)
    public void onMessage(ConsumerRecord<String, String> consumerRecord){
        if(consumerRecord == null){
            return;
        }
        log.info("search服务收到了创建文章的消息:{}", consumerRecord.value());
        Long articleId = Long.parseLong(consumerRecord.value());
        searchService.onArticleCreate(articleId);
    }
}
