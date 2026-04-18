package xyz.nanian.owl.crow.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.crow.domain.dto.CreateConversationDTO;
import xyz.nanian.owl.crow.domain.entity.ConversationDO;
import xyz.nanian.owl.crow.mapper.ConversationMapper;
import xyz.nanian.owl.crow.mapper.MessageMapper;
import xyz.nanian.owl.crow.service.ConversationService;
import xyz.nanian.owl.crow.domain.vo.ConversationVO;
import xyz.nanian.owl.crow.domain.vo.MessageVO;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.util.List;
import java.util.UUID;

/**
 * 会话管理
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationServiceImpl implements ConversationService {

    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    /**
     * 创建会话ID，
     * @param dto
     * @return
     */
    @Override
    public String createConversation(CreateConversationDTO dto) {

        String conversationId = UUID.randomUUID().toString();

        log.info("conversationId:{}",conversationId);

        ConversationDO conversation = new ConversationDO();
        conversation.setId(conversationId);
        conversation.setTitle(dto != null ? dto.getTitle() : "新对话");
//        获取用户账号，
        conversation.setUserId(UserContext.getUserCode());

        log.info("conversationId:{}",conversation);

        conversationMapper.insert(conversation);

        return conversationId;
    }

    @Override
    public List<ConversationVO> listCurrentUserConversations() {
//        return conversationMapper.selectByUserId(
////                SecurityUtil.getCurrentUserId()
//        );
        return null;
    }

    @Override
    public List<MessageVO> getMessages(String conversationId) {
        return messageMapper.selectByConversationId(conversationId);
    }

    @Override
    public void deleteConversation(String conversationId) {
        conversationMapper.deleteById(conversationId);
    }
}

