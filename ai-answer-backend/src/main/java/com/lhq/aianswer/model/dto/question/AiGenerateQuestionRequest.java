package com.lhq.aianswer.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 上午11:34
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {
    
    /**
     * 应用id
     */
    private Long appId;
    
    /**
     * 题目数量
     */
    private int questionNumber = 10;
    
    /**
     * 选项数量
     */
    private int optionNumber = 2;
    
    private static final long serialVersionUID = 1L;
}
