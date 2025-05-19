package com.lhq.aianswer.manager;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.aspectj.bridge.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 上午10:50
 */
@Component
public class AiManager {
    @Resource
    private ClientV4 clientV4;
    
    private static final float STABLE_TEMPERATURE = 0.05f;
    private static final float UNSTABLE_TEMPERATURE = 0.95f;
    
    /**
     * 通用同步请求（答案较稳定）
     * @param systemMessages
     * @param userMessages
     * @return
     */
    public String doSyncUnstableRequest(String systemMessages, String userMessages) {
        return doSyncRequest(systemMessages, userMessages, UNSTABLE_TEMPERATURE);
    }
    
    /**
     * 通用同步请求（答案较稳定）
     * @param systemMessages
     * @param userMessages
     * @return
     */
    public String doSyncStableRequest(String systemMessages, String userMessages) {
        return doSyncRequest(systemMessages, userMessages, STABLE_TEMPERATURE);
    }
    
    /**
     * 通用同步请求
     * @param systemMessages
     * @param userMessages
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessages, String userMessages, Float temperature) {
        return doRequest(systemMessages, userMessages, Boolean.FALSE, temperature);
    }
    
    /**
     * 通用请求，简化了消息传递
     * @param systemMessages
     * @param userMessages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessages, String userMessages, Boolean stream, Float temperature) {
        // 创建一个ArrayList来存储ChatMessage对象
        ArrayList<ChatMessage> messages = new ArrayList<>();
        // 创建一个ChatMessage对象，角色为SYSTEM，内容为systemMessages
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessages);
        // 创建一个ChatMessage对象，角色为USER，内容为userMessages
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessages);
        // 将systemMessage添加到messages中
        messages.add(systemMessage);
        // 将userMessage添加到messages中
        messages.add(userMessage);
        // 调用doRequest方法，传入messages，stream和temperature参数
        return doRequest(messages, stream, temperature);
    }
    
    /**
     * 用于初始化 AiManager 对象
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model(Constants.ModelChatGLM4)
            .stream(stream)
            .temperature(temperature)
            .messages(messages)
            .build();
        ModelApiResponse invokeModelApi = clientV4.invokeModelApi(chatCompletionRequest);
        ChatMessage result = invokeModelApi.getData().getChoices().get(0).getMessage();
        return result.getContent().toString();
    }
}
