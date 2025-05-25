package com.lhq.aianswer.model.dto.user_answer;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 下午10:17
 */
@Data
public class QuestionAnswerDTO {
    
    /**
     * 用户答案
     */
    private String title;
    
    /**
     * 用户答案
     */
    private String userAnswer;
    
}
