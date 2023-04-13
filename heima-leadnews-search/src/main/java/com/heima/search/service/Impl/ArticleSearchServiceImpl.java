package com.heima.search.service.Impl;


import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.threadlocal.UserThreadLocalUtils;
import com.heima.common.util.JsonUtils;
import com.heima.feign.ArticleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.media.dtos.WmNewsContentItem;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.ArticleSearchService;
import com.heima.search.service.SearchHistoryService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ArticleSearchServiceImpl implements ArticleSearchService {

    @Autowired
    private SearchHistoryService historyService;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ArticleClient articleClient;

    private final static String indexName = "app_info_article";

    @Override
    public void importArticle(List<ArticleDto> articleDtoList) {
        if (CollectionUtils.isEmpty(articleDtoList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (ArticleDto dto : articleDtoList) {
            if (StringUtil.isEmpty(dto.getStaticUrl())) {
                continue;
            }
            IndexRequest indexRequest = new IndexRequest(indexName).id("" + dto.getId());
            Map<String, Object> map = buildEsArticle(dto);
            indexRequest.source(map, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        if (bulkRequest.numberOfActions() > 0) {
            try {
                client.bulk(bulkRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ArticleDto> search(UserSearchDto dto) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders
                .multiMatchQuery(dto.getSearchWords()
                        , "title", "content", "labels"));
        if (dto.getMinBehotTime() != null) {
            boolQueryBuilder.filter(QueryBuilders
                    .rangeQuery("publishTime").lt(dto.getMinBehotTime()));
        }
        //分页
        searchRequest.source().query(boolQueryBuilder)
                .from(dto.getFromIndex()).size(dto.getPageSize());
        //排序
        searchRequest.source().sort("publishTime", SortOrder.DESC);
        //高亮
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.preTags("<em>");
        highlight.field("title");
        highlight.postTags("<em>");
        highlight.requireFieldMatch(false);
        searchRequest.source().highlighter(highlight);
        List<ArticleDto> articleList = new ArrayList<>();
        try {
            //查询
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //结果解析
            SearchHits hits = search.getHits();
            for (SearchHit hit : hits) {
                ArticleDto articleDto = JsonUtils.toBean(hit.getSourceAsString(), ArticleDto.class);
                //高亮处理
                HighlightField title = hit.getHighlightFields().get("title");
                if (title != null) {
                    Text[] fragments = title.getFragments();
                    if (fragments != null && fragments.length > 0) {
                        articleDto.setTitle(fragments[0].toString());
                    }
                }
                articleList.add(articleDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("搜索ES异常:{}", e.getMessage());
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
        if(!CollectionUtils.isEmpty(articleList)){
            // 异步的去保存搜索的历史记录
            historyService.asyncSaveSearchHistory(UserThreadLocalUtils.getUserId(), dto.getEquipmentId(), dto.getSearchWords());
        }
        return articleList;

    }

    @Override
    public void onArticleCreate(Long articleId) {
        // 因为消息是异步的，访问查询article查不到，sleep
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertToES(articleId);
    }

    private void insertToES(Long articleId) {
        IndexRequest indexRequest = new IndexRequest(indexName).id("" + articleId);
        ArticleDto articleDto = articleClient.findById(articleId);
        if (articleDto == null) {
            log.error("根据articleId查询不到article数据,articleId:{}", articleId);
            return;
        }
        Map<String, Object> map = buildEsArticle(articleDto);
        indexRequest.source(map, XContentType.JSON);
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存es的index异常:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    private Map<String, Object> buildEsArticle(ArticleDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", dto.getId());
        map.put("publishTime", dto.getPublishTime());
        map.put("layout", dto.getLayout());
        map.put("images", dto.getImages());
        map.put("staticUrl", dto.getStaticUrl());
        map.put("authorName", dto.getAuthorName());
        map.put("labels", dto.getLabels());
        map.put("channelName", dto.getChannelName());
        map.put("title", dto.getTitle());
        map.put("content", dto.getContent());
        return map;
    }
}