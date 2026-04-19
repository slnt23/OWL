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
@Schema(name= "UserAddressDO对象", description = "用户收货地址表")
public class UserAddressDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(name = "用户ID")
    private Long userId;

    /**
     * 收件人姓名
     */
    @Schema(name = "收件人姓名")
    @TableField("receiver_name")
    private String receiverName;

    /**
     * 收件人电话
     */
    @Schema(name = "收件人电话")
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 省
     */
    @Schema(name = "省")
    @TableField("province")
    private String province;

    /**
     * 城
     */
    @TableField("city")
    @Schema(name = "城")
    private String city;

    /**
     * 县
     */
    @Schema(name = "县")
    @TableField("district")
    private String district;

    /**
     * 详细地址
     */
    @TableField("detail")
    @Schema(name = "详细地址")
    private String detail;

    /**
     * 是否默认地址
     */
    @TableField("is_default")
    @Schema(name = "是否默认地址")
    private Byte isDefault;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
