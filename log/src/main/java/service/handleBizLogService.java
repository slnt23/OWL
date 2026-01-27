package service;


import DTO.BizLogMessage;

/**
 * 处理业务日志，
 *
 * @author slnt23
 * @since 2026/1/27
 */

public interface handleBizLogService {

    public void addBizLog(BizLogMessage message);
}
