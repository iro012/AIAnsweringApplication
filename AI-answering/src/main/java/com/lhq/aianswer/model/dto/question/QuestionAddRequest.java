package com.lhq.aianswer.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建问题请求
 *
 * @author  lhq
 * @date  25-04-2025 00:11:06
 * @version 1.0
 */

@Data
public class QuestionAddRequest implements Serializable {
    
    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;
    
    /**
     * 应用 id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}