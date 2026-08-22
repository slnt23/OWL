package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 站长个人信息 VO
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "站长个人信息 VO")
public class ProfileVO {

    /**
     * 头像 URL
     */
    @Schema(description = "头像 URL")
    private String avatarUrl;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    private String name;

    /**
     * 一行标签
     */
    @Schema(description = "一行标签")
    private String tagline;

    /**
     * 个人简介
     */
    @Schema(description = "个人简介")
    private String bio;

    /**
     * 所在地
     */
    @Schema(description = "所在地")
    private String location;

    /**
     * GitHub 链接
     */
    @Schema(description = "GitHub 链接")
    private String githubUrl;

    /**
     * 个人网站
     */
    @Schema(description = "个人网站")
    private String websiteUrl;

    /**
     * 联系邮箱
     */
    @Schema(description = "联系邮箱")
    private String email;

    /**
     * CodeTime UID
     */
    @Schema(description = "CodeTime UID")
    private String codetimeUid;
}
