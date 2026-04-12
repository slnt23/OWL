package xyz.nanian.owl.crow;


import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.crow.vo.ConversationVO;
import xyz.nanian.owl.crow.vo.MessageVO;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 会话管理
 *
 * @author slnt23
 * @since 2026/4/12
 */

public interface ConversationApi {

    /**
     * 创建会话
     * @return
     */
    Result<String> createConversation();

    /**
     *
     * @return
     */
    Result<List<ConversationVO>>  listConversations();

    /**
     *
     * @return
     */
    Result<List<MessageVO>> listMessages();

    /**
     *
     * @param conversationId
     * @return
     */
    Result<ResultStatus> delete(String conversationId);
}
