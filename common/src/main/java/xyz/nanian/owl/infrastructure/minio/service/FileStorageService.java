package xyz.nanian.owl.infrastructure.minio.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * 文件对象存储
 *
 * @author slnt23
 * @since 2026/4/19
 */

public interface FileStorageService {
    String upload(MultipartFile file, String bucketName);     // 新增 bucketName 参数

    String getUrl(String bucketName, String objectName);

    void delete(String bucketName, String objectName);

    // 可选：保留旧方法作为默认实现（单个桶场景）
    default String upload(MultipartFile file) {
        return upload(file, "temp");   // 默认使用 temp 桶
    }
}
