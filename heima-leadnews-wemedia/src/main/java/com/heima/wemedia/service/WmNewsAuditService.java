package com.heima.wemedia.service;

import com.heima.model.media.dtos.WmNewsDto;
import com.heima.wemedia.entity.WmNews;

public interface WmNewsAuditService {


    void auditWmNews(Integer wmUserId, WmNewsDto dto);
}