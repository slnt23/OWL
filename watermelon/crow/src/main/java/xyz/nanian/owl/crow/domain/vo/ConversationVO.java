package xyz.nanian.owl.crow.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 返回会话列表
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Data
@Builder
@Schema(name = "返回会话列表VO")
public class ConversationVO {

    /**
     * 会话id
     */
    private String id;

    /**
     * 会话title
     */
    private String title;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

