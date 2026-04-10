package xyz.nanian.owl.sugarcane;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;


/**
 * 代码生成
 *
 * @author slnt23
 * @since 2026/4/3
 */


public class CodeGenerator {

    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/owl?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8",
                        "root",
                        "123456qin"
                )
                .globalConfig(builder -> {
                    builder
                            .author("slnt23")
                            .outputDir(projectPath + "/src/main/java/sugarcane")
                            .disableOpenDir();
                })

                .packageConfig(builder -> {
                    builder
                            .parent("xyz.nanian.owl")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            // XML 输出到 resources/mapper
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper"
                            ));
                })

                .strategyConfig(builder -> {
                    builder
                            .addInclude("sys_user", "sys_role", "sys_menu")
                            .addTablePrefix("sys_")

                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()

                            .mapperBuilder()
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()

                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")

                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle();
                })

                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}
