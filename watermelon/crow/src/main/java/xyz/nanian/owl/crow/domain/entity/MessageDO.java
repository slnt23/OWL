package xyz.nanian.owl.crow.domain.entity;

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
public class MessageDO {

    /**
     * 消息主键（自增）
     */
    private Long id;

    /**
     * 所属会话ID（外键关联 conversation）
     */
    private String conversationId;

    /**
     * 消息角色：user / assistant / system
     */
    private String role;

    /**
     * 消息具体内容
     */
    private String content;

    /**
     * 该消息token数
     */
    private Integer tokenCount;

    /**
     * 使用的模型名称
     */
    private String model;

    /**
     * 生成终止原因（stop / length / content_filter 等）
     */
    private String finishReason;

    /**
     * 消息创建时间
     */
    private LocalDateTime createdAt;
}

