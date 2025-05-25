package com.lhq.aianswer.config;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/22 下午11:19
 */
@Configuration
@Data
public class VipSchedulerConfig {

    @Bean
    public Scheduler vipSchedulers() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger atomicInteger =  new AtomicInteger(1);
            
            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r, "vip-thread-" + atomicInteger.getAndIncrement());
                // 设置为非守护线程
                thread.setDaemon(false);
                return thread;
            }
        };
        ExecutorService executorService = Executors.newScheduledThreadPool(10,threadFactory);
        return Schedulers.from(executorService);
    }
}
