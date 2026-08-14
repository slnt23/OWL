package xyz.nanian.owl.log.service;

import xyz.nanian.owl.log.domain.message.OperationLogMessage;

/**
 * Handles persisted operation logs.
 */
public interface OperationLogRecordService {

    void addOperationLog(OperationLogMessage message);
}
