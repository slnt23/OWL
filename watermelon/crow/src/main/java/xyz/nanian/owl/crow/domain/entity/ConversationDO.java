package xyz.nanian.owl.crow.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("conversation")
public class ConversationDO {

    /**
     * 主键UUID
     */
    @TableId(type = IdType.INPUT) // 你用UUID，需要手动赋值
    @NotBlank
    private String id;

    /**
     * 用户ID（数据库字段：user_code）
     */
    @TableField("user_code")
    @NotBlank
    private String userCode;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 累计token消耗
     */
    @TableField("total_tokens")
    @NotNull
    private Integer totalTokens;

    /**
     * 软删除标记
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
