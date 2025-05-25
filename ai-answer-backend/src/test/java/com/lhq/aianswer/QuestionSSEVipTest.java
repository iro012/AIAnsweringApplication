package com.lhq.aianswer;

import com.lhq.aianswer.controller.QuestionController;
import com.lhq.aianswer.model.dto.question.AiGenerateQuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/23 上午11:08
 */
@SpringBootTest
public class QuestionSSEVipTest {
    
    @Resource
    private QuestionController questionController;
    
    @Test
    public void aiGenerateQuestionSSEVIPTest() throws InterruptedException {
        AiGenerateQuestionRequest request = new AiGenerateQuestionRequest();
        request.setAppId(3L);
        request.setQuestionNumber(10);
        request.setOptionNumber(2);
        
        questionController.aiGenerateQuestionSseTest(request, false);
        questionController.aiGenerateQuestionSseTest(request, false);
        questionController.aiGenerateQuestionSseTest(request, true);
        
        Thread.sleep(1000000L);
    }
    
}
