package com.lhq.aianswer.model.dto.question;

import com.lhq.aianswer.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询问题请求
 *
 * @author  lhq
 * @date  25-04-2025 00:11:06
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 题目内容（json格式）
     */
    private String questionContent;
    
    /**
     * 应用 id
     */
    private Long appId;
    
    /**
     * 创建用户 id
     */
    private Long userId;
    
    

    private static final long serialVersionUID = 1L;
}