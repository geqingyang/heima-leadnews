package com.heima.common.dtos;

import lombok.Setter;

@Setter
public class PageRequestDto {
    protected Long size;
    protected Long page;

    public Long getSize() {
        if (this.size == null || this.size <= 0 || this.size > 100) {
            setSize(10L);
        }
        return size;
    }

    public Long getPage() {
        if (this.page == null || this.page <= 0) {
            setPage(1L);
        }
        return page;
    }
}