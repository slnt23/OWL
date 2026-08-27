package xyz.nanian.owl.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户收货地址表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-19 13:02:36
 */
@Getter
@Setter
@ToString
@TableName("user_address")
@Schema(name = "用户收货地址表", description = "用户收货地址表")
public class UserAddressDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "收件人姓名")
    @TableField("receiver_name")
    private String receiverName;

    @Schema(description = "收件人电话")
    @TableField("receiver_phone")
    private String receiverPhone;

    @Schema(description = "省")
    @TableField("province")
    private String province;

    @TableField("city")
    @Schema(description = "城市")
    private String city;

    @Schema(description = "区/县")
    @TableField("district")
    private String district;

    @TableField("detail")
    @Schema(description = "详细地址")
    private String detail;

    @TableField("is_default")
    @Schema(description = "是否默认地址")
    private Integer isDefault;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}