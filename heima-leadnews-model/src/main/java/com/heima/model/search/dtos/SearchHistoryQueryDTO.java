package com.heima.model.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryQueryDTO {

    private String equipmentId;

    private Integer pageSize;
}
