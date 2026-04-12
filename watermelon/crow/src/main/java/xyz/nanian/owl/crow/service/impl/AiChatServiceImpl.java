package xyz.nanian.owl.crow.service.impl;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.crow.domain.dto.ChatRequestDTO;
import xyz.nanian.owl.crow.service.AiChatService;

/**
 * ai
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Service
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;

    public AiChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * chat
     * @param dto
     * @return
     */
    @Override
    public String chat(ChatRequestDTO dto) {
        return chatClient.prompt()
                .user(dto.getMessage())
                .call()
                .content();
    }
}
