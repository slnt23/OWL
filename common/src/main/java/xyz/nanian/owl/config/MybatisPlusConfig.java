package xyz.nanian.owl.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * mybatis-plus 配置拦截器
 *
 * @author slnt23
 * @since 2025/11/8
 */

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        mysql默认数据库，添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        乐观锁，防止多人同时修改，导致出错
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    /**
     * 配置 SqlSessionFactory，并设置下划线转驼峰映射
     */
//    @Bean
//    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(DataSource dataSource,
//                                                                     MybatisPlusInterceptor mybatisPlusInterceptor) {
//        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//
//        // 设置插件（分页 + 乐观锁）
//        factoryBean.setPlugins(mybatisPlusInterceptor);
//
//        // ==================== 关键：配置映射规则 ====================
//        Configuration configuration = new Configuration();
//        configuration.setMapUnderscoreToCamelCase(true);   // 下划线转驼峰（推荐开启）
//        // 可选其他常用设置
//        // configuration.setLogImpl(StdOutImpl.class);     // 打印 SQL 日志（开发环境）
//        // configuration.setJdbcTypeForNull(JdbcType.NULL);
//
//        factoryBean.setConfiguration(configuration);
//
//        return factoryBean;
//    }
}
