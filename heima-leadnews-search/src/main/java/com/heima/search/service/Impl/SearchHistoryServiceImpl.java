package com.heima.search.service.Impl;

import com.heima.common.threadlocal.UserThreadLocalUtils;
import com.heima.model.search.dtos.SearchHistoryQueryDTO;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.SearchHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private static final String KEY_PREFIX = "leadnews:search:";

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Override
    public void asyncSaveSearchHistory(Integer userId, String equipmentId, String searchWords) {
        String key = buildKey(userId,equipmentId);
        redisTemplate.boundZSetOps(key).incrementScore(searchWords,1);
        
    }

    @Override
    public List<String> load(SearchHistoryQueryDTO dto) {
        String key = buildKey(UserThreadLocalUtils.getUserId(), dto.getEquipmentId());
        Set<String> set = redisTemplate.boundZSetOps(key).reverseRangeByScore(0, Double.MAX_VALUE);
        return new ArrayList<>(set);
    }

    @Override
    public void deleteSearchWord(UserSearchDto dto) {
        String key = buildKey(UserThreadLocalUtils.getUserId(), dto.getEquipmentId());
        Long remove = redisTemplate.boundZSetOps(key).remove(dto.getSearchWords());
        log.info("搜索记录删除，成功删除:{}条记录",remove);
    }

    private String buildKey(Integer userId, String equipmentId) {
        if (userId == null){
            //说明是匿名用户
            return KEY_PREFIX + equipmentId;
        }else {
            return KEY_PREFIX + userId;
        }
    }
}
