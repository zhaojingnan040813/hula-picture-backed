package com.hula.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 赵景南
 * @Date: 2025/1/7 22:21
 * @Version: v1.0.0
 * @Description: 略
 **/
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

