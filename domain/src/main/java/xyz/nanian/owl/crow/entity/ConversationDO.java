package xyz.nanian.owl.crow.entity;

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
public class ConversationDO {

    /**
     * 会话ID（UUID，主键）
     */
    private String id;

    /**
     * 用户ID（支持多用户）
     */
    private String userId;

    /**
     * 对话标题（可由AI自动生成或用户手动修改）
     */
    private String title;

    /**
     * 累计token消耗
     */
    private Integer totalTokens;

    /**
     * 是否删除（软删除：0=正常，1=已删除）
     */
    private Integer deleted;

    /**
     * 会话创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 会话最后更新时间
     */
    private LocalDateTime updatedAt;
}
