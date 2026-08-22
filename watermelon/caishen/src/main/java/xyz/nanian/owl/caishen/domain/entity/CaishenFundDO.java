package xyz.nanian.owl.caishen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基金档案。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@TableName("caishen_fund")
public class CaishenFundDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 基金代码，如 000001 */
    @TableField("fund_code")
    private String fundCode;

    /** 基金名称 */
    @TableField("fund_name")
    private String fundName;

    /** 基金类型：股票型/混合型/债券型/货币型/指数型 */
    @TableField("fund_type")
    private String fundType;

    /** 状态：1=正常，0=停用 */
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
