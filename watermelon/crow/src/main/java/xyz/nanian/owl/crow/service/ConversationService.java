package xyz.nanian.owl.crow.service;


import xyz.nanian.owl.crow.domain.dto.CreateConversationDTO;
import xyz.nanian.owl.crow.domain.vo.ConversationVO;
import xyz.nanian.owl.crow.domain.vo.MessageVO;

import java.util.List;

/**
 * 会话管理
 *
 * @author slnt23
 * @since 2026/4/12
 */

public interface ConversationService {

    String createConversation(CreateConversationDTO dto);

    List<ConversationVO> listCurrentUserConversations();

    List<MessageVO> getMessages(String conversationId);

    void deleteConversation(String conversationId);
}
