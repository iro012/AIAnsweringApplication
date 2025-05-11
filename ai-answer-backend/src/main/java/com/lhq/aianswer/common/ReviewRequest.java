package com.lhq.aianswer.common;

import lombok.Data;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/4/28 上午9:59
 */
@Data
public class ReviewRequest {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 审核信息
     */
    private String reviewMessage;
    
    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;
}
