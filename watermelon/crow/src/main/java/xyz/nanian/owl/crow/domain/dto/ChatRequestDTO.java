package xyz.nanian.owl.crow.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用于发送消息
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Data
@Schema(name = "发送消息DTO")
public class ChatRequestDTO {
    
    @Schema(description = "会话ID")
    @NotBlank
    private String conversationId;

    @Schema(description = "消息内容")
    @NotBlank
    private String message;
}