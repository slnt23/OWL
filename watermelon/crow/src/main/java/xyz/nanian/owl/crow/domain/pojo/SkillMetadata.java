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
    private String name;
    private String description;
    private String version;
    private String promptTemplate;
    private Map<String, Object> parameters;

    // getter/setter
}
