package com.heima.article.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.heima.article.entity.ApArticle;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.constants.message.Article2EsConstants;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JsonUtils;
import com.heima.file.service.MinioService;
import com.heima.model.article.dtos.ArticleDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.heima.common.constants.message.Article2EsConstants.ARTICLE_2_ES_TOPIC;

@Service
@Slf4j
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

    @Autowired
    private Configuration configuration;

    @Autowired
    private MinioService minioService;

    @Lazy //延迟注入
    @Autowired
    private ApArticleService apArticleService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 生成静态文件上传到minIO中
     *
     * @param apArticle
     * @param content
     */
    @Async //异步操作
    @Override
    public void buildContentHtml(ApArticle apArticle, String content) {
        //已知文章的id
        //4.1 获取文章内容
        if (StringUtils.isNotBlank(content)) {
            //4.2 文章内容通过freemarker生成html文件
            Template template = null;
            StringWriter out = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                //数据模型
                Map<String, Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                //合成
                template.process(contentDataModel, out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //4.3 把html文件上传到minio中
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = minioService.uploadHtmlFile("", apArticle.getId() + ".html", in);


            //4.4 修改ap_article表，保存static_url字段
            UpdateWrapper<ApArticle> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().
                    eq(ApArticle::getId, apArticle.getId()).
                    set(ApArticle::getStaticUrl, path);
            apArticleService.update(updateWrapper);

            //        发送Es创建索引消息

            kafkaTemplate.send(Article2EsConstants.ARTICLE_2_ES_TOPIC, ""+apArticle.getId());
        }
    }
}
