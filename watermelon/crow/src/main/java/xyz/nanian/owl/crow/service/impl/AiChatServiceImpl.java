package xyz.nanian.owl.crow.service.impl;


import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.crow.domain.dto.ChatRequestDTO;
import xyz.nanian.owl.crow.domain.entity.MessageDO;
import xyz.nanian.owl.crow.mapper.ConversationMapper;
import xyz.nanian.owl.crow.mapper.MessageMapper;
import xyz.nanian.owl.crow.service.AiChatService;

import xyz.nanian.owl.crow.constant.AIConstant;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ai
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Service
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final MessageMapper messageMapper;
    private final ConversationMapper conversationMapper;

    public AiChatServiceImpl(ChatClient chatClient,
                             MessageMapper messageMapper,
                             ConversationMapper conversationMapper) {
        this.chatClient = chatClient;
        this.messageMapper = messageMapper;
        this.conversationMapper = conversationMapper;
    }

    /**
     * chat
     * @param dto
     * @return
     */
    @SneakyThrows
    @Override
    public String chat(ChatRequestDTO dto) {

        MessageDO messageUser = new MessageDO();
        messageUser.setContent(dto.getMessage());
        messageUser.setConversationId(dto.getConversationId());
        messageUser.setRole(AIConstant.ROLE_USER);
        messageUser.setCreatedAt(LocalDateTime.now());
//        messageUser.set

//        1.保存用户消息
        messageMapper.insert(messageUser);

//        2. 查询历史,取出历史消息，
        List<MessageDO> history = messageMapper.selectRecentMessages(dto.getConversationId(),
                AIConstant.RecentMessageNumberLimit);

//        3. 转换，AI格式（Message列表）
        List<Message> messages = history.stream()
                .map(this::convertMessageDO)
                .toList();

//        4. 调用模型
        ChatResponse resp = chatClient.prompt()
                .messages(messages)
                .call()
                .chatResponse();

//        5. 拿到回复
        String reply = null;
        if (resp != null) {
            reply = resp.getResult().getOutput().getText();
        }

//        获取消耗的token
        Usage usage= null;
        if (resp != null) {
            usage = resp.getMetadata().getUsage();
        }

//        6. 本轮消息保存回复，
        MessageDO messageAI = new MessageDO();
        messageAI.setContent(reply);
        messageAI.setConversationId(dto.getConversationId());
        messageAI.setRole(AIConstant.ROLE_ASSISTANT);
        messageAI.setCreatedAt(LocalDateTime.now());
        if (usage != null) {
            messageAI.setPromptTokens(usage.getPromptTokens());
        }
        if (usage != null) {
            messageAI.setCompletionTokens(usage.getCompletionTokens());
        }
        if (usage != null) {
            messageAI.setTotalTokenCount(usage.getTotalTokens());
        }
        messageMapper.insert(messageAI);

//        7. 更新本轮对话累计token消耗总数，以及当前这段会话的最后对话的时间，
        if (usage != null) {
            conversationMapper.updateTotalTokens(dto.getConversationId(),usage.getTotalTokens());
        }

//        8. 回复前端
        return reply;
    }


    /**
     * 消息转换
     * @param messageDO
     * @return
     */
    private Message convertMessageDO(MessageDO messageDO) {
        return switch (messageDO.getRole()) {
            case AIConstant.ROLE_USER -> new UserMessage(messageDO.getContent());
            case AIConstant.ROLE_ASSISTANT -> new AssistantMessage(messageDO.getContent());
            case AIConstant.ROLE_SYSTEM -> new SystemMessage(messageDO.getContent());
            default -> throw new IllegalArgumentException(
                    AIConstant.ROLE_ILLEGAL + messageDO.getRole()
            );
        };
    }

}
