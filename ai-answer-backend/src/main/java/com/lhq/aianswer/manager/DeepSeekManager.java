package com.lhq.aianswer.manager;


import com.volcengine.ark.runtime.model.completion.chat.*;
import com.volcengine.ark.runtime.service.ArkService;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 下午5:54
 */
@Component
public class DeepSeekManager {
    
    @Resource
    private ArkService arkService;
    
    // 较稳定的随机数
    private static final double STABLE_TEMPERATURE = 0.05;
    
    // 不稳定的随机数
    private static final double UNSTABLE_TEMPERATURE = 0.95;
    
    
    /**
     * 通用流式请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ChatCompletionChunk> doStreamRequest(String systemMessage, String userMessage, Double temperature) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemMessage).build();
        messages.add(systemChatMessage);
        ChatMessage userChatMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userMessage).build();
        messages.add(userChatMessage);
        return doStreamRequest(messages, STABLE_TEMPERATURE);
    }
    
    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    public Flowable<ChatCompletionChunk> doStreamRequest(List<ChatMessage> messages, Double temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model("deepseek-v3-250324")
            .stream(Boolean.TRUE)
            .temperature(temperature)
            .messages(messages)
            .build();
        return arkService.streamChatCompletion(chatCompletionRequest);
    }
    
    /**
     * 同步调用（答案较稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doSyncRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
    }
    
    /**
     * 同步调用（答案较随机）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doSyncRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
    }
    
    
    public String doSyncRequest(String systemMessage, String userMessage, Double temperature) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }
    
    
    /**
     * 通用请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Double temperature) {
        // 构造请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemMessage).build();
        messages.add(systemChatMessage);
        ChatMessage userChatMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userMessage).build();
        messages.add(userChatMessage);
        return doRequest(messages, stream, temperature);
    }
    
    /**
     * 通用请求
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages, Boolean stream, Double temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model("deepseek-v3-250324")
            .stream(stream)
            .temperature(temperature)
            .messages(messages)
            .build();
        ChatCompletionResult chatCompletion = arkService.createChatCompletion(chatCompletionRequest);
        ChatMessage result = chatCompletion.getChoices().get(0).getMessage();
        return result.getContent().toString();
    }
}
