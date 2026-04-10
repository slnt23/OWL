package xyz.nanian.owl.log.DTO;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 业务日志，MQ传输对象
 *
 * @author slnt23
 * @since 2026/1/27
 */

@Data
@ToString
public class BizLogMessageDTO implements Serializable {

    private String module;
    private String action;
    private String userCode;
    private String method;
    private Boolean success;
    private Long cost;
    private String errorMsg;
    private String traceId;

}
