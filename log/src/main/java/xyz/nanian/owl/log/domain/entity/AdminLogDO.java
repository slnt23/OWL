package xyz.nanian.owl.log.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Admin operation log entity.
 */
@Data
@TableName("admin_log")
public class AdminLogDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long adminId;
    private String action;
    private String detail;
    private String ip;
    private LocalDateTime createTime;
}
