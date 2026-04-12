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
@Schema(name = "返回会话列表")
public class ConversationVO {

    private String id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

