package xyz.nanian.owl.crow.service.impl;


import io.minio.MinioClient;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.crow.domain.pojo.SkillMetadata;
import xyz.nanian.owl.crow.service.SkillRegistryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态运用skill
 *
 * @author slnt23
 * @since 2026/4/25
 */

@Component
public class MinioSkillRegistryService implements SkillRegistryService {

    private final MinioClient minioClient; // OSS 客户端
    private final Map<String, SkillMetadata> cache = new HashMap<>();

    public MinioSkillRegistryService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // 从 OSS 下载 Skill 配置或模板
    @Override
    public SkillMetadata get(String skillName) {
        if (cache.containsKey(skillName)) {
            return cache.get(skillName);
        }


        SkillMetadata metadata = downloadFromOss(skillName);

        cache.put(skillName, metadata);
        return metadata;
    }

    // OSS 列目录，读取所有 Skill 配置的轻量信息
    @Override
    public List<SkillMetadata> listAll() {
        // OSS 列目录，读取所有 Skill 配置的轻量信息
        List<SkillMetadata> list = listSkillsFromOss();
        return list;
    }

    // OSS 下载文件、解析 JSON 或模板
    private SkillMetadata downloadFromOss(String skillName) {
        // OSS 下载文件、解析 JSON 或模板
        SkillMetadata metadata = new SkillMetadata();
        metadata.setName(skillName);
        metadata.setDescription("从 OSS 加载的 Skill");
        metadata.setTemplate("...模板内容...");
        return metadata;
    }

    private List<SkillMetadata> listSkillsFromOss() {
        // 只读取文件名和描述
//        return Arrays.asList(
//                new SkillMetadata("skill1", "演示 Skill 1", null),
//                new SkillMetadata("skill2", "演示 Skill 2", null)
//        );
        return null;
    }
}

