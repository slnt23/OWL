package DO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务日志实体类
 *
 * @author slnt23
 * @since 2026/1/27
 */

@Data
@TableName("biz_log")
public class BizLogDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String module;
    private String action;

    private Long userId;
    private String method;

    private Boolean success;
    private Long cost;

    private String errorMsg;
    private String traceId;

    private LocalDateTime createTime;
}

