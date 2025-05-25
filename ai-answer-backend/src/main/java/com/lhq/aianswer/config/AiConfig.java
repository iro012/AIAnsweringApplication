package com.lhq.aianswer.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/19 上午10:44
 */
@Deprecated
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {
    private String aiKey;
    
    @Bean
    public ClientV4 getClientV4() {
        return new ClientV4.Builder(aiKey).build();
    }
}
