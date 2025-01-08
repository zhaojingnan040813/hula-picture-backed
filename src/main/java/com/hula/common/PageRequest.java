package com.hula.common;

import lombok.Data;

/**
 * @Author: 赵景南
 * @Date: 2025/1/7 22:21
 * @Version: v1.0.0
 * @Description: 略
 **/
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}

