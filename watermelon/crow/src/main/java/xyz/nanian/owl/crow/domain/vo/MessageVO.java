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
@Schema(name = "返回历史消息VO")
public class MessageVO {

    @Schema(description = "消息ID")
    private Long id;

    @Schema(description = "消息角色，user/assistant/system")
    private String role;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "消息创建时间")
    private LocalDateTime createTime;
}