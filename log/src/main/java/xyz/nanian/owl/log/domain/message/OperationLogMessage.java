package xyz.nanian.owl.log.domain.message;

import lombok.Data;
import lombok.ToString;
import xyz.nanian.owl.log.constant.LogType;

import java.io.Serializable;

/**
 * Operation log message sent through MQ.
 */
@Data
@ToString
public class OperationLogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private LogType logType;
    private String module;
    private String action;
    private Long operatorId;
    private String method;
    private Boolean success;
    private Long cost;
    private String errorMsg;
    private String traceId;
}
