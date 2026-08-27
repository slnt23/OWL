package xyz.nanian.owl.crow.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Message 实体类
 * 对应数据库表：message
 * @author slnt23
 * @since 2026/4/12
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("agent_message")
@Schema(name = "消息表", description = "AI对话消息表")
public class MessageDO {

    @Schema(description = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "会话ID")
    @TableField("conversation_id")
    private String conversationId;

    @Schema(description = "角色")
    private String role;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "输入token数")
    @TableField("prompt_tokens")
    private Integer promptTokens;

    @Schema(description = "输出token数")
    @TableField("completion_tokens")
    private Integer completionTokens;

    @Schema(description = "总token数")
    @TableField("total_token_count")
    private Integer totalTokenCount;

    @Schema(description = "模型")
    private String model;

    @Schema(description = "结束原因")
    @TableField("finish_reason")
    private String finishReason;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}