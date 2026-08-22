package xyz.nanian.owl.caishen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关注基金。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@TableName("caishen_fund_watch")
public class CaishenFundWatchDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 ID，关联 user.id */
    @TableField("user_id")
    private Long userId;

    /** 基金代码 */
    @TableField("fund_code")
    private String fundCode;

    /** 用户备注，如"定投基金" */
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
