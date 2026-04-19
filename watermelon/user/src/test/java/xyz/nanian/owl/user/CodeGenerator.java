package xyz.nanian.owl.user;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;


/**
 * 代码生成
 *
 * @author slnt23
 * @since 2026/4/3
 */


public class CodeGenerator {

    public static void main(String[] args) {

//        String projectPath = System.getProperty("user.dir");
        String projectPath = "D:/IT/IDEA/Dev/OWL/watermelon/user";

        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/pitaya?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8",
                        "root",
                        "123456qin"
                )
                .globalConfig(builder -> {
                    builder
                            .author("slnt23")
                            .enableSwagger()
                            .outputDir(projectPath + "/src/main/java")
                            .disableOpenDir()
                            .commentDate("yyyy-MM-dd HH:mm:ss");
                })

                .packageConfig(builder -> {
                    builder.parent("xyz.nanian.owl.user")
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
                            .addInclude("role", "user_address", "user_role")
//                            .addTablePrefix("price_")

                            .entityBuilder()
                                .enableLombok()
                                .enableTableFieldAnnotation()
                                .formatFileName("%sDO")

                            .mapperBuilder()
                                .mapperAnnotation(Mapper.class)
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
