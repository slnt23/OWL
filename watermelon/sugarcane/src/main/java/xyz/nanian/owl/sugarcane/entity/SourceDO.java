package xyz.nanian.owl.sugarcane.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 价格来源表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Getter
@Setter
@TableName("price_source")
@Schema(description = "价格来源表")
public class SourceDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "来源名称（如：国家统计局、XX批发市场）", example = "国家统计局")
    @TableField("source_name")
    private String sourceName;

    @Schema(description = "来源类型：API-接口，CRAWLER-爬虫，MANUAL-人工录入", example = "API")
    @TableField("source_type")
    private String sourceType;

    @Schema(description = "来源地址", example = "https://data.stats.gov.cn/")
    @TableField("source_url")
    private String sourceUrl;

    @Schema(description = "可靠等级1-5（5为最高）", example = "5")
    @TableField("reliability_level")
    private Integer reliabilityLevel;

    @Schema(description = "备注", example = "官方数据来源，可信度高")
    @TableField("remark")
    private String remark;

    @Schema(description = "状态：1-启用，0-禁用", example = "1")
    @TableField("status")
    private Byte status;

    @Schema(description = "创建时间", example = "2025-04-12T08:00:00")
    @TableField("created_at")
    private LocalDateTime createdAt;
}
