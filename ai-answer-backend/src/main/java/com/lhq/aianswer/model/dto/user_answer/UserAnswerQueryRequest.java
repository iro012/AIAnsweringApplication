package com.lhq.aianswer.model.dto.user_answer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lhq.aianswer.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询用户答案请求
 *
 * @author  lhq
 * @date  25-04-2025 00:09:07
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAnswerQueryRequest extends PageRequest implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 应用 id
     */
    private Long appId;
    
    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    private Integer appType;
    
    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;
    
    /**
     * 用户答案（JSON 数组）
     */
    private String choices;
    
    /**
     * 评分结果 id
     */
    private Long resultId;
    
    /**
     * 结果名称，如物流师
     */
    private String resultName;
    
    /**
     * 结果描述
     */
    private String resultDesc;
    
    /**
     * 得分
     */
    private Integer resultScore;
    
    /**
     * 用户 id
     */
    private Long userId;
    
    private static final long serialVersionUID = 1L;
}