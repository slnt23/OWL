package xyz.nanian.owl.log.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User operation log entity.
 */
@Data
@TableName("user_log")
public class UserLogDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String module;
    private String action;
    private String method;
    private Boolean success;
    private Long cost;
    private String errorMsg;
    private String traceId;
    private LocalDateTime createTime;
}
