package xyz.nanian.owl.caishen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 仅映射 user 表的 id + email，供定时任务批量取收件邮箱。
 * caishen 模块保持自包含，不依赖 user 模块。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@TableName("user_account")
public class CaishenUserEmailDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("email")
    private String email;
}
