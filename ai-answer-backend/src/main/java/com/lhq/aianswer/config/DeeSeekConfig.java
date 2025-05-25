package com.lhq.aianswer.config;

import com.volcengine.ark.runtime.service.ArkService;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 下午5:55
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class DeeSeekConfig {
    
    private String apiKey;
    
    private String baseUrl;
    
    @Bean
    public ArkService getArkService() {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        return ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl(baseUrl).apiKey(apiKey).build();
    }
}
