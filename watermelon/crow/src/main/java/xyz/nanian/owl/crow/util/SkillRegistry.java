package xyz.nanian.owl.crow.util;


import xyz.nanian.owl.crow.domain.pojo.SkillMetadata;

import java.util.List;

/**
 * 动态管理Skill,从过从OSS中选择自己使用的skill，
 *
 * @author slnt23
 * @since 2026/4/25
 */

public interface SkillRegistry {
    SkillMetadata get(String skillName);        // 获取某个 Skill 的完整元数据和内容
    List<SkillMetadata> listAll();             // 列出所有 Skill 的轻量级元数据
}
