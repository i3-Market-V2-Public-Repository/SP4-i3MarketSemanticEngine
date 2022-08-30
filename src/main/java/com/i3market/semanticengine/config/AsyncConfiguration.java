package com.i3market.semanticengine.config;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync( proxyTargetClass=true)
public class AsyncConfiguration
{
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
}

//
//    public Executor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(3);
//        executor.setMaxPoolSize(3);
//        executor.setQueueCapacity(100);
//        executor.setThreadNamePrefix("AsynchThread-");
//        executor.initialize();
//        return executor;
//    }

//    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(Integer.parseInt(String.valueOf(25)));
//                executor.setQueueCapacity(100);
//                executor.setMaxPoolSize(Integer.parseInt(String.valueOf(25)));
//                executor.setThreadNamePrefix("response_executor_thread");
//                executor.initialize();
//                return executor;