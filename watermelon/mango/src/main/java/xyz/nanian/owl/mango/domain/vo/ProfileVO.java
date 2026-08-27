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

    @Schema(description = "头像 URL")
    private String avatarUrl;

    @Schema(description = "显示名称")
    private String name;

    @Schema(description = "一行标签")
    private String tagline;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "所在地")
    private String location;

    @Schema(description = "GitHub 链接")
    private String githubUrl;

    @Schema(description = "个人网站")
    private String websiteUrl;

    @Schema(description = "联系邮箱")
    private String email;

    @Schema(description = "CodeTime UID")
    private String codetimeUid;
}