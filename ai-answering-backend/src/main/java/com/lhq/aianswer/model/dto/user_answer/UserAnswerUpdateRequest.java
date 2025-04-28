package com.lhq.aianswer.model.dto.user_answer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新用户答案请求
 *
 * @author  lhq
 * @date  25-04-2025 00:09:07
 * @version 1.0
 */
@Data
public class UserAnswerUpdateRequest implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 应用 id
     */
    private Long appId;
    
    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;
    
    private static final long serialVersionUID = 1L;
}