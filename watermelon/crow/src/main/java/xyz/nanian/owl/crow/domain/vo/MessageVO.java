package xyz.nanian.owl.crow.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 返回历史消息
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Data
@Builder
@Schema(name = "返回历史消息DTO")
public class MessageVO {

    private Long id;

    private String role;

    private String content;

    private LocalDateTime createdAt;
}

