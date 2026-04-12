package xyz.nanian.owl.sugarcane.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地理位置表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
/**
 * 地理位置表
 */
@Getter
@Setter
@TableName("geo_location")
@Schema(description = "地理位置表")
public class GeoLocationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "地点名称（如：北京市、朝阳区）", example = "北京市")
    @TableField("location_name")
    private String locationName;

    @Schema(description = "类型：COUNTRY-国家，CITY-城市，DISTRICT-区县，STORE-门店",
            example = "CITY")
    @TableField("location_type")
    private String locationType;

    @Schema(description = "父级区域ID（关联本表id）", example = "10001")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "经度（GCJ-02坐标系）", example = "116.4074")
    @TableField("longitude")
    private BigDecimal longitude;

    @Schema(description = "纬度（GCJ-02坐标系）", example = "39.9042")
    @TableField("latitude")
    private BigDecimal latitude;

    @Schema(description = "GeoHash编码（用于快速地理位置检索）", example = "wx4g0")
    @TableField("geo_hash")
    private String geoHash;

    @Schema(description = "状态：1-启用，0-禁用", example = "1")
    @TableField("status")
    private Byte status;

    @Schema(description = "创建时间", example = "2025-04-12T10:30:00")
    @TableField("created_at")
    private LocalDateTime createdAt;
}

