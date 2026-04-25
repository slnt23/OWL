package xyz.nanian.owl.infrastructure.minio.service.impl;


import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.nanian.owl.infrastructure.minio.service.FileStorageService;
import xyz.nanian.owl.infrastructure.minio.properties.MinioProperties;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * minio 实现类
 *
 * @author slnt23
 * @since 2026/4/19
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * 项目启动时自动创建所有在配置文件中声明的 Bucket
     */
    @PostConstruct
    public void init() {
        List<MinioProperties.BucketConfig> bucketList = minioProperties.getBuckets();

        if (bucketList == null || bucketList.isEmpty()) {
            log.warn("MinIO buckets 配置为空，请检查 storage.minio.buckets 配置");
            return;
        }

        for (MinioProperties.BucketConfig bucketConfig : bucketList) {
            String bucketName = bucketConfig.getName();

            if (bucketName == null || bucketName.trim().isEmpty()) {
                log.warn("检测到空的 bucket name，已跳过");
                continue;
            }

            try {
                boolean exists = minioClient.bucketExists(
                        BucketExistsArgs.builder().bucket(bucketName).build()
                );

                if (!exists) {
                    minioClient.makeBucket(
                            MakeBucketArgs.builder().bucket(bucketName).build()
                    );
                    log.info("MinIO Bucket 创建成功: {}", bucketName);
                } else {
                    log.debug("Bucket 已存在，跳过创建: {}", bucketName);
                }
            } catch (Exception e) {
                log.error("创建 MinIO Bucket 失败: {}", bucketName, e);
            }
        }
    }

    @Override
    public String upload(MultipartFile file, String bucketName) {
        try {
            // 生成带日期目录的文件名（推荐结构）
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String ext = getFileExtension(file.getOriginalFilename());
            String objectName = datePath + "/" + UUID.randomUUID() + ext;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // 返回预签名URL（推荐方式，更安全）
//            return getUrl(bucketName, objectName);
//            这里改为OSS的存储路径，
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public String getUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(3600)        // 1小时有效期
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("生成文件URL失败", e);
        }
    }

    @Override
    public void delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }

    // 工具方法
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".bin";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
