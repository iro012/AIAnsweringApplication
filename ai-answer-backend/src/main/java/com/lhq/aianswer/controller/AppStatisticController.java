package com.lhq.aianswer.controller;

import com.lhq.aianswer.common.BaseResponse;
import com.lhq.aianswer.common.ResultUtils;
import com.lhq.aianswer.mapper.UserAnswerMapper;
import com.lhq.aianswer.model.dto.statistic.AppAnswerCountDTO;
import com.lhq.aianswer.model.dto.statistic.AppAnswerResultCountDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/23 下午6:08
 */
@RestController
@RequestMapping("/app/statistic")
public class AppStatisticController {
    
    @Resource
    private UserAnswerMapper userAnswerMapper;
    
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAnswerCount());
    }
    
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAnswerResultCount(@RequestParam Long appId) {
        return ResultUtils.success(userAnswerMapper.doAnswerResultCount(appId));
    }
}
