package xyz.nanian.owl.mango.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 博客站长个人信息实体类，对应数据库表：blog_profile（单行配置表，id 固定为 1）
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_profile")
@Schema(name = "站长信息表", description = "站长个人信息表")
public class BlogProfileDO {

    @Schema(description = "主键ID，固定为 1")
    @TableId(type = IdType.INPUT)
    private Long id;

    @Schema(description = "头像 URL")
    @TableField("avatar_url")
    private String avatarUrl;

    @Schema(description = "显示名称")
    private String name;

    @Schema(description = "一行标签")
    private String tagline;

    @Schema(description = "个人简介（支持 Markdown）")
    private String bio;

    @Schema(description = "所在地")
    private String location;

    @Schema(description = "GitHub 链接")
    @TableField("github_url")
    private String githubUrl;

    @Schema(description = "个人网站")
    @TableField("website_url")
    private String websiteUrl;

    @Schema(description = "联系邮箱")
    private String email;

    @Schema(description = "CodeTime UID（用于徽章）")
    @TableField("codetime_uid")
    private String codetimeUid;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}