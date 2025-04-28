package com.lhq.aianswer.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑问题请求
 *
 * @author  lhq
 * @date  25-04-2025 00:11:06
 * @version 1.0>
 */
@Data
public class QuestionEditRequest implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;
    
    /**
     * 应用 id
     */
    private Long appId;
    
}