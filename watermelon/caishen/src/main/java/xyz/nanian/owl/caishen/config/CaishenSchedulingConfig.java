package xyz.nanian.owl.caishen.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时调度配置。
 *
 * <p>caishen 是本项目第一个引入定时调度的模块，{@code @EnableScheduling}
 * 限定在本模块配置内，不影响其他模块。提供一个专用 {@link ThreadPoolTaskScheduler}，
 * 使 {@code @Scheduled} 任务使用独立线程池，单任务异常不终止调度线程。</p>
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Slf4j
@Configuration
@EnableScheduling
public class CaishenSchedulingConfig {

    @Bean("caishenTaskScheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(3);
        scheduler.setThreadNamePrefix("caishen-scheduler-");
        scheduler.setErrorHandler(t -> log.error("[caishen] 定时任务执行异常", t));
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        return scheduler;
    }
}
