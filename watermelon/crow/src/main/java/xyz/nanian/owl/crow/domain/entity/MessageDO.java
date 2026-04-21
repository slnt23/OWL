package xyz.nanian.owl.crow.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("message")
public class MessageDO {

    /**
     * 主键自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    @TableField("conversation_id")
    private String conversationId;

    /**
     * 角色
     */
    private String role;

    /**
     * 内容
     */
    private String content;

    /**
     * 输入token
     */
    @TableField("prompt_tokens")
    private Integer promptTokens;

    /**
     * 输出token
     */
    @TableField("completion_tokens")
    private Integer completionTokens;

    /**
     * 总token
     */
    @TableField("total_token_count")
    private Integer totalTokenCount;

    /**
     * 模型
     */
    private String model;

    /**
     * 结束原因
     */
    @TableField("finish_reason")
    private String finishReason;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}


