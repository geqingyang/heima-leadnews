package com.heima.search.service;

import com.heima.model.search.dtos.SearchHistoryQueryDTO;
import com.heima.model.search.dtos.UserSearchDto;

import java.util.List;

public interface SearchHistoryService {
    /**
     * 用户搜索完成以后，异步保存搜索的历史记录
     * */
    void asyncSaveSearchHistory(Integer userId, String equimentId, String searchWords);

    List<String> load(SearchHistoryQueryDTO dto);

    void deleteSearchWord(UserSearchDto dto);
}
