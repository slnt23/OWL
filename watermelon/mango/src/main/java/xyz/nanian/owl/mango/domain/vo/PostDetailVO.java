package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章详情 VO，含 Markdown 正文
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文章详情 VO")
public class PostDetailVO extends PostVO {

    @Schema(description = "Markdown 正文")
    private String content;
}