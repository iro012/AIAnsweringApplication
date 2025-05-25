package com.lhq.aianswer.model.dto.statistic;

import lombok.Data;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/23 下午5:38
 */
@Data
public class AppAnswerCountDTO {
    /**
     * 应用id
     */
    private String appId;
    
    /**
     * 答案数量
     * */
    private Integer count;
}
