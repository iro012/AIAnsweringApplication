package com.lhq.aianswer.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/4/25 下午11:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionContentDTO {
    
    private String title;
    
    private List<Option> options;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Option {
       private String result;
       private int score;
       private String key;
       private String value;
    }
}
