package com.hula.model.dto.picture;

import lombok.Data;

import java.util.List;

@Data
public class PictureUploadByBatchRequest {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 抓取数量
     */
    private Integer count = 10;

    /**
     * 名称前缀
     * 在Service层设置它与 searchText 拼接
     */
    private String namePrefix;

    /**
     * 标签
     */
    private List<String> tags;  // 在数据库里面存储的是一个JSON，但是你传给前端的是一个list 方便它转换

    /**
     * 分类
     */
    private String category;
}
