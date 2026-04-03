package xyz.nanian.owl.pitaya;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * 业务代码生成,当前的代码只是示例，在后续创建新的模块的时候可以使用，
 *
 * @author slnt23
 * @since 2026/3/27
 */

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/owl?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
                        "root",
                        "123456qin"
                )
                .globalConfig(builder -> {
                    builder.author("slnt")
                            .disableOpenDir()
                            .outputDir(System.getProperty("user.dir") + "/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("xyz.nanian.owl.pitaya")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user") // 比如 user
                            .addTablePrefix("t_", "sys_"); // 可选：去掉表前缀

                    builder.entityBuilder()
                            .enableLombok();

                    builder.mapperBuilder()
                            .enableMapperAnnotation();

                    builder.serviceBuilder()
                            .formatServiceFileName("%sService");

                    builder.controllerBuilder()
                            .enableRestStyle();
                })
                .execute();
    }
}

