package com.lhq.aianswer.model.dto.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.lhq.aianswer.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询应用请求
 *
 * @author  lhq
 * @date  25-04-2025 00:10:47
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryRequest extends PageRequest implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
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
    
    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;
    
    /**
     * 审核信息
     */
    private String reviewMessage;
    
    /**
     * 审核人 id
     */
    private Long reviewerId;
    
    /**
     * 审核时间
     */
    private Date reviewTime;
    
    /**
     * 创建用户 id
     */
    private Long userId;
    
    
    /**
     * 搜索词
     */
    private String searchText;
    
    private static final long serialVersionUID = 1L;
}