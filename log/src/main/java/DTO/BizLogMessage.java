package DTO;


import lombok.Data;

import java.io.Serializable;

/**
 * 业务日志，MQ传输对象
 *
 * @author slnt23
 * @since 2026/1/27
 */

@Data
public class BizLogMessage implements Serializable {

    private String module;
    private String action;
    private Long userId;
    private String method;
    private Boolean success;
    private Long cost;
    private String errorMsg;
    private String traceId;

}
