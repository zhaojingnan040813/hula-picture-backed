package com.hula.model.dto.file;

import lombok.Data;

import java.util.List;

/**
 * 这是一个 dto
 */
@Data
public class UploadPictureResult {

    /**
     * 图片地址
     */
    private String url;

    /**
     * 图片名称
     */
    private String picName;

    /**
     * 文件体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private int picWidth;

    /**
     * 图片高度
     */
    private int picHeight;

    /**
     * 图片宽高比
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 标签
     */
    private List<String> tags;  // 在数据库里面存储的是一个JSON，但是你传给前端的是一个list 方便它转换

    /**
     * 分类
     */
    private String category;

    /**
     * 缩略图 url
     */
    private String thumbnailUrl;

    /**
     * 图片主色调
     */
    private String picColor;


}
