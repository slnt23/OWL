package xyz.nanian.owl.user.domain.entity;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_settings")
@Schema(name = "博客设置表")
public class BlogSettingsDO {

    @Schema(description = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "所属用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "标签行")
    @TableField("tag_line")
    private String tagLine;

    @Schema(description = "简介段落数组（JSON）")
    @TableField("bio")
    private String bio;

    @Schema(description = "位置信息")
    private String location;

    @Schema(description = "GitHub 链接")
    @TableField("github_url")
    private String githubUrl;

    @Schema(description = "CodeTime Badge URL")
    @TableField("codetime_url")
    private String codetimeUrl;

    @Schema(description = "诗句/座右铭")
    private String poem;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}