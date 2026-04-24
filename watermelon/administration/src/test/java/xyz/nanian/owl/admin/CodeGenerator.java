package xyz.nanian.owl.admin;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;

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
        String projectPath = "D:/IT/IDEA/Dev/OWL/watermelon/administration";

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
                    builder.parent("xyz.nanian.owl.admin")
                            .entity("domain.entity")
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
                            .addInclude("spotlight", "feature")
//                            .addTablePrefix("price_")

                            .entityBuilder()
                                .enableLombok()
                                .enableTableFieldAnnotation()
                                .formatFileName("%sDO")

                            .mapperBuilder()
                                .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)  // ← 这里是重点修改
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
