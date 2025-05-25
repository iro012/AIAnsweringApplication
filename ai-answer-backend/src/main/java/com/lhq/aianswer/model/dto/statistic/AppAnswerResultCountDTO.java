package com.lhq.aianswer.model.dto.statistic;

import lombok.Data;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/23 下午5:59
 */
@Data
public class AppAnswerResultCountDTO {
    /**
     * 答案结果
     */
    private String resultName;
    
    /**
     * 答案数量
     */
    
    private Integer count;
}
