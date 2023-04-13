package com.heima.search.test;


import com.heima.feign.ArticleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.search.service.ArticleSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Db2Es {

    @Autowired
    private ArticleClient articleFeign;

    @Autowired
    private ArticleSearchService searchService;

    @Test
    public void testDb2Es()  throws Exception{

        int page=1,size=2;
        while(true) {
//        远程调用article服务，获取文章数据
            List<ArticleDto> articleDtoList = articleFeign.findArticleByPage(page, size);
            if(CollectionUtils.isEmpty(articleDtoList)){
                break;
            }
            System.out.println("#######page=="+page);
//        调用方法导入Es
            searchService.importArticle(articleDtoList);
            if(articleDtoList.size()<size){
                break;
            }
            page++;
        }
    }
}
