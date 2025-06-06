package com.lhq.aianswer.model.dto.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建应用请求
 *
 * @author  lhq
 * @date  25-04-2025 00:10:47
 * @version 1.0
 */

@Data
public class AppAddRequest implements Serializable {
    
    /**
     * 应用名
     */
    private String appName;
    
    /**
     * 应用描述
     */
    private String appDesc;
    
    /**
     * 应用图标
     */
    private String appIcon;
    
    /**
     * 应用类型（0-得分类，1-测评类）
     */
    private Integer appType;
    
    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;
    

    private static final long serialVersionUID = 1L;
}