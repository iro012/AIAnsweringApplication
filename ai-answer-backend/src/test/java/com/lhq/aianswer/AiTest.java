package com.lhq.aianswer;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 上午10:25
 */
@SpringBootTest
public class AiTest {
  
    /**
     * 同步调用
     */
    @Test
    public void testInvoke() {
        
        ClientV4 client = new ClientV4.Builder("61ee949d981e4b18afd6f98d7b58b305.o7emgQceKRAM2YMR").build();
        
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "作为一名营销专家，请为智谱开放平台创作一个吸引人的slogan");
        messages.add(chatMessage);
        String requestId = String.valueOf(System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model(Constants.ModelChatGLM4)
            .stream(Boolean.FALSE)
            .invokeMethod(Constants.invokeMethod)
            .messages(messages)
            .requestId(requestId)
            .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        System.out.println("model output:" + invokeModelApiResp.getMsg());
    }
}
