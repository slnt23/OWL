package xyz.nanian.owl.crow.domain.pojo;


import lombok.Data;

import java.util.Map;

/**
 * skill
 *
 * @author slnt23
 * @since 2026/4/25
 */

@Data
public class SkillMetadata {
    private String id;
    private String name;
    private String description;
    private String version;
    private String template;   //  prompt 模板内容，
    private String ossPath;  // OSS文件路径
    private Map<String, Object> parameters;  //  其他参数
}
