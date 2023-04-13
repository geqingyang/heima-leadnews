package com.heima.wemedia.controller.v1;


import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsPageReqDto;
import com.heima.wemedia.entity.WmNews;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 分页查询新闻内容
     * @param dto
     * @return
     */
    @PostMapping("/page")
    public PageResult<WmNews> findByPage(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.findByPage(dto);
    }

    /**
     * 自媒体端新增保存修改文章
     * @param wmNewsDto
     * @return
     */
    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto wmNewsDto){
        wmNewsService.submit(wmNewsDto);
        return ResponseResult.ok();
    }

    @GetMapping("/one/{id}")
    public ResponseResult<WmNews> getById(@PathVariable("id") Integer id){
        return ResponseResult.ok(wmNewsService.getById(id));
    }

    /**
     * 通过卡夫卡实现文章上下架
     * @param dto
     * @return
     */
    @PutMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto){
        wmNewsService.downOrUp(dto);
        return ResponseResult.ok();
    }

}
