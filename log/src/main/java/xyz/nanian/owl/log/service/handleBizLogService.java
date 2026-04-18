package xyz.nanian.owl.log.service;


import xyz.nanian.owl.log.domain.dto.BizLogMessageDTO;

/**
 * 处理业务日志，
 *
 * @author slnt23
 * @since 2026/1/27
 */

public interface handleBizLogService {

    void addBizLog(BizLogMessageDTO message);
}
