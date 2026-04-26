package xyz.nanian.owl.crow.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.crow.domain.pojo.SkillMetadata;
import xyz.nanian.owl.crow.service.SkillRegistryService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * skill
 *
 * @author slnt23
 * @since 2026/4/26
 */

@Service
@RequiredArgsConstructor
public class SkillExecutorService {

    private final SkillRegistryService skillRegistry;
    private final ChatClient chatClient;

    // 可选缓存 SkillMetadata 避免每次都从 OSS 下载
    private final Map<String, SkillMetadata> skillCache = new ConcurrentHashMap<>();

    /**
     * 执行 Skill
     * @param skillName Skill 名称
     * @param input 参数映射
     * @return AI 执行结果
     */
    public String execute(String skillName, Map<String,Object> input) {
        SkillMetadata skill = getSkill(skillName);
        if (skill == null) {
            throw new IllegalArgumentException("Skill not found: " + skillName);
        }

        // 1. 获取模板
        String template = skill.getTemplate();
        if (template == null || template.isEmpty()) {
            // 如果模板在 OSS 文件中，需要下载
            template = downloadTemplateFromOSS(skill);
        }

        // 2. 填充模板
        String filledPrompt = fillTemplate(template, input);

        // 3. 调用 AI 接口
        return chatWithAI(filledPrompt);
    }

    private SkillMetadata getSkill(String skillName) {
        // 优先从缓存获取
        return skillCache.computeIfAbsent(skillName, skillRegistry::get);
    }

    private String downloadTemplateFromOSS(SkillMetadata skill) {
        // 调用你的 OSS 下载逻辑
        // 这里假设返回文件内容
//        return ossService.download(skill.getOssPath());

        return null;
    }

    private String fillTemplate(String template, Map<String,Object> input) {
        // 简单占位符替换，支持 {{key}} 格式
        String result = template;
        for (Map.Entry<String,Object> entry : input.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
        }
        return result;
    }

    /**
     * 封装 ChatClient 调用
     */
    private String chatWithAI(String prompt) {
        // Spring AI 最新调用方式
//        return chatClient.chat(ChatMessage.ofUser(prompt)).blockOptional().orElse("AI 未返回结果");
        return null;
    }
}

