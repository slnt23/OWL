package xyz.nanian.owl.crow.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Conversation 实体类
 * 对应数据库表：conversation
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("agent_conversation")
@Schema(name = "会话表", description = "AI对话会话表")
public class ConversationDO {

    @Schema(description = "主键UUID")
    @TableId(type = IdType.INPUT)
    @NotBlank
    private String id;

    @Schema(description = "用户ID")
    @TableField("user_code")
    @NotBlank
    private String userCode;

    @Schema(description = "对话标题")
    private String title;

    @Schema(description = "累计token消耗")
    @TableField("total_tokens")
    @NotNull
    private Integer totalTokens;

    @Schema(description = "软删除标记")
    @TableLogic
    private Integer deleted;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}