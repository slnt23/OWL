package xyz.nanian.owl.crow.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String conversationId;

    @NotBlank
    private String message;
}

