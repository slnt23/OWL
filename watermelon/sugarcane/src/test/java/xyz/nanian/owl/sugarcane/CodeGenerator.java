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

//        String projectPath = System.getProperty("user.dir");
        String projectPath = "D:/IT/IDEA/Dev/OWL/watermelon/sugarcane";

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
                    builder.parent("xyz.nanian.owl.sugarcane")
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
                            .addInclude("price_category", "price_item", "price_source","price_record","geo_location")
                            .addTablePrefix("price_")

                            .entityBuilder()
                                .enableLombok()
                                .enableTableFieldAnnotation()
                                .formatFileName("%sDO")

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
