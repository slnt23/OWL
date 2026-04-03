package xyz.nanian.owl.log.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 日志
 *
 * @author slnt23
 * @since 2026/1/28
 */

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("xyz.nanian.owl.log")
public class LogAutoConfig {
}
