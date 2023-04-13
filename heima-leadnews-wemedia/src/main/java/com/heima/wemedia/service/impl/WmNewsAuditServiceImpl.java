package com.heima.wemedia.service.impl;

import com.heima.common.aliyun.GreenImageUploadScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.delayTask.RedisDelayedQueueUtil;
import com.heima.common.enums.WeMediaStatusEnum;
import com.heima.common.tess4j.Tess4jClient;
import com.heima.common.util.JsonUtils;
import com.heima.common.util.SensitiveWordUtil;
import com.heima.feign.ArticleClient;
import com.heima.file.service.MinioService;
import com.heima.model.media.dtos.WmNewsContentItem;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import com.heima.wemedia.entity.WmNews;
import com.heima.wemedia.entity.WmSensitive;
import com.heima.wemedia.service.WmNewsAuditService;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmSensitiveService;
import jodd.util.ArraysUtil;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WmNewsAuditServiceImpl implements WmNewsAuditService, InitializingBean {

    @Autowired
    private WmNewsService newsService;

    @Autowired
    private ArticleClient articleClient;

    @Autowired
    private GreenTextScan textScan;

    @Autowired
    private GreenImageUploadScan imageScan;

    @Autowired
    private MinioService minioService;

    @Autowired
    private WmSensitiveService sensitiveService;

    @Autowired
    private Tess4jClient tess4jClient;

    @Autowired
    private RedisDelayedQueueUtil delayedQueueUtil;

    private final static String DELAYED_QUEUE_AUDIT = "delayed_queue_audit";

    private ExecutorService taskMsgService = Executors.newSingleThreadExecutor();

    private ExecutorService businessService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors()*2,
            120L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 自媒体文章审核
     * @param wmUserId
     * @param dto
     */
    @Override
    @Async  //当前是异步方法
    public void auditWmNews(Integer wmUserId, WmNewsDto dto) {
        log.info("异步执自媒体文章审核");
        String contentToBeAudit = buildContent(dto.getContent(), dto.getTitle(), dto.getLabels());
        List<byte[]> imageByteList = buildImageByteList(dto);
//        敏感字审核
        boolean ret = dfaSensitiveWordsAudit(contentToBeAudit,dto);
        if (!ret){
            return;
        }
//        OCR图片敏感字审核
        ret = ocrSensitiveWordsAudit(imageByteList, dto);
        if(!ret){
            return;
        }

//        阿里云文本审核
        ret = aliyunTextAudit(contentToBeAudit, dto);
        if(!ret){
            return;
        }
//        阿里云图片审核
        ret = aliyunImageAudit(imageByteList, dto);
        if(!ret){
            return;
        }
        // 审核通过
        updateStatus(8, null, dto.getId(), "审核通过");
        Date publishTime = dto.getPublishTime();
        if(publishTime != null && publishTime.getTime() > System.currentTimeMillis()){
            // 延迟发布
            WmNewsResultDTO newsResultDTO = buildWmNewsResultDto(wmUserId, dto);
            String json = JsonUtils.toString(newsResultDTO);
            delayedQueueUtil.addQueue(json, publishTime.getTime()-System.currentTimeMillis(), TimeUnit.MILLISECONDS, DELAYED_QUEUE_AUDIT);
        }else{
            // 立即发布
            // 保存article
            log.info("远程调用article接口创建article");
            WmNewsResultDTO newsResultDTO = buildWmNewsResultDto(wmUserId, dto);
            Long articleId = articleClient.createArticle(newsResultDTO);
            // 更新wmNews的数据库的状态
            this.updateStatus(9, articleId, dto.getId(), "");
        }
    }



    private boolean aliyunImageAudit(List<byte[]> imageByteList, WmNewsDto dto) {
        log.info("阿里云图片审核");
        if (CollectionUtils.isEmpty(imageByteList)){
            return true;
        }
        try{
            Map<String, String> resultMap = imageScan.imageScan(imageByteList);
            return parseResult(resultMap,dto.getId());

        }catch (Exception e){
            e.printStackTrace();
            log.error("阿里云图片审核异常:{}",e.getMessage());
            updateStatus(2,null,dto.getId(),e.getMessage());
            return false;
        }
    }

    private boolean aliyunTextAudit(String contentToBeAudit, WmNewsDto dto) {
        log.info("阿里云文本审核");
        try {
            Map<String, String> resultMap = textScan.greenTextScan(contentToBeAudit);
            return parseResult(resultMap,dto.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("阿里云文本审核异常:{}",e.getMessage());
            updateStatus(2,null,dto.getId(),e.getMessage());
            return false;
        }
    }

    private boolean parseResult(Map<String, String> resultMap, Integer id) {
        String suggestion = resultMap.get("suggestion");
        String reason = resultMap.get("reason");
        if ("pass".equals(suggestion)){
            return true;
        }
        if ("block".equals(suggestion)){
            updateStatus(2,null,id,reason);
            return false;
        }
        if ("review".equals(suggestion)){
            updateStatus(3,null,id,reason);
            return false;
        }
        return true;
    }

    private boolean ocrSensitiveWordsAudit(List<byte[]> imageByteList, WmNewsDto dto) {
        try {
            if (CollectionUtils.isEmpty(imageByteList)){
                return true;
            }
            for (byte[] bytes : imageByteList) {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
                String content = tess4jClient.doOCR(bufferedImage);
                return dfaSensitiveWordsAudit(content,dto);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("图片敏感字审核异常:{}",e.getMessage());
            updateStatus(2,null,dto.getId(),e.getMessage());
            return false;
        }
    }

    private boolean dfaSensitiveWordsAudit(String contentToBeAudit, WmNewsDto dto) {
        Collection<String> words = buildSensitiveWords();
        String content = contentToBeAudit;
        Map<String, Integer> resultMap = SensitiveWordUtil.matchWords(words, content);
        if (CollectionUtils.isEmpty(resultMap)){
            return true;
        }
        String reason = resultMap.keySet().stream().collect(Collectors.joining(";"));
        this.updateStatus(2,null,dto.getId(),reason);
        return false;
    }

    private Collection<String> buildSensitiveWords() {
        List<WmSensitive> list = sensitiveService.list();
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
    }

    private List<byte[]> buildImageByteList(WmNewsDto dto) {
        List<byte[]> result = new ArrayList<>();
        //content
        List<WmNewsContentItem> imageItems = WmNewsContentItem.getImageItems(dto.getContent());
        if (CollectionUtils.isEmpty(imageItems)){
            return null;
        }
        for (WmNewsContentItem imageItem : imageItems) {
            byte[] bytes = minioService.downLoadFile(imageItem.getValue());
            if (ArrayUtils.isNotEmpty(bytes)){
                result.add(bytes);
            }
        }
        //cover
        List<String> images = dto.getImages();
        if (images != null){
            for (String image : images) {
                byte[] bytes = minioService.downLoadFile(image);
                if (bytes!=null){
                    result.add(bytes);
                }
            }
        }

        return result;

    }

    private String buildContent(String content, String title, String labels) {
        StringBuilder builder = new StringBuilder();
        List<WmNewsContentItem> textItems = WmNewsContentItem.getTextItems(content);
        //详情中的数据
        if (!CollectionUtils.isEmpty(textItems)){
            builder.append(textItems.stream().map(WmNewsContentItem::getValue)
                    .collect(Collectors.joining("-")));
        }
        //title中的数据
        builder.append(title).append("-");
        //label中的数据
        builder.append(labels);
        return builder.toString();
    }

    private WmNewsResultDTO buildWmNewsResultDto(Integer wmUserId, WmNewsDto dto) {
        WmNewsResultDTO result = new WmNewsResultDTO();
        result.setId(dto.getId());
        result.setWmUserId(wmUserId);
        result.setTitle(dto.getTitle());
        result.setContent(dto.getContent());
        result.setType(dto.getType());
        result.setChannelId(dto.getChannelId());
        result.setChannelName(null);
        result.setLabels(dto.getLabels());
        result.setPublishTime(new Date());
        if (!CollectionUtils.isEmpty(dto.getImages())){
            result.setImages(dto.getImages().stream()
            .collect(Collectors.joining(",")));
        }
        return result;
    }

    private void updateStatus(int status, Long articleId, Integer wmNewsId, String reason) {
        WmNews wmNews = new WmNews();
        wmNews.setId(wmNewsId);
        wmNews.setStatus(status);
        wmNews.setArticleId(articleId);
        wmNews.setReason(reason);
        newsService.updateById(wmNews);
    }

    /**
     * 框架启动完成以后，启动死循环，去redisson的延迟队列中取任务执行
     * */
    @Override
    public void afterPropertiesSet() throws Exception{
        taskMsgService.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        String value = delayedQueueUtil.getDelayQueue(DELAYED_QUEUE_AUDIT);
                        log.info("收到延时任务:value={}",value);
                        businessService.execute(new Runnable() {
                            @Override
                            public void run() {
                                afterTaskMsg(value);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void afterTaskMsg(String json) {
        if (StringUtils.isEmpty(json)){
            return;
        }
        WmNewsResultDTO newsResultDTO = JsonUtils.toBean(json, WmNewsResultDTO.class);
        Long articleId = articleClient.createArticle(newsResultDTO);
        this.updateStatus(WeMediaStatusEnum.SUCCESS_AUDIT.getStatus()
                ,articleId,newsResultDTO.getId(),"");
    }
}
