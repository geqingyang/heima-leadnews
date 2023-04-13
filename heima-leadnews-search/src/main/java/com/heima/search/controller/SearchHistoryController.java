package com.heima.search.controller;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.search.dtos.SearchHistoryQueryDTO;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchHistoryController {

    @Autowired
    private SearchHistoryService historyService;

    /**
     * 查询用户的搜索的历史记录
     *
     */
    @PostMapping("/api/v1/history/load")
    public ResponseResult<List<String>> load(@RequestBody SearchHistoryQueryDTO dto){
        List<String> list = historyService.load(dto);
        return ResponseResult.ok(list);
    }

    /**
     * 删除用户的搜索记录
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/history/del")
    public ResponseResult del(@RequestBody UserSearchDto dto){
        historyService.deleteSearchWord(dto);
        return ResponseResult.ok();
    }

}
