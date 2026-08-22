package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.api.domain.dto.PageDTO;

/**
 * 文章分页列表查询参数
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文章分页查询参数")
public class PostQueryDTO extends PageDTO {

    /**
     * 按标签筛选
     */
    @Schema(description = "按标签筛选")
    private Long tagId;

    /**
     * 按分类筛选
     */
    @Schema(description = "按分类筛选")
    private Long categoryId;

    /**
     * 标题/摘要关键词搜索
     */
    @Schema(description = "标题/摘要关键词搜索")
    private String keyword;

    /**
     * 按语言筛选：zh / en
     */
    @Schema(description = "按语言筛选：zh / en")
    private String lang;

    /**
     * 是否包含草稿，仅登录后有效，默认 false
     */
    @Schema(description = "是否包含草稿，仅登录后有效")
    private Boolean includeDraft;
}
