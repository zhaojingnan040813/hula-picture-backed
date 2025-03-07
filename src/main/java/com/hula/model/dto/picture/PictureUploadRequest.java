package com.hula.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureUploadRequest implements Serializable {

    /**
     * 图片 id（用于修改）
     */
    private Long id;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 图片名称  这个是后面才添加的
     */
    private String picName;

    private static final long serialVersionUID = 1L;

    /**
     * 标签
     */
    private List<String> tags;  // 在数据库里面存储的是一个JSON，但是你传给前端的是一个list 方便它转换

    /**
     * 分类
     */
    private String category;

    /**
     * 空间 id
     */
    private Long spaceId;


}

