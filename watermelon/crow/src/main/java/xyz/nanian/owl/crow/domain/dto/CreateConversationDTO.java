package xyz.nanian.owl.crow.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建初始化标题
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Data
@Schema(name = "创建初始化标题")
public class CreateConversationDTO {

    /**
     * 会话title
     */
    private String title;
}

