package xyz.nanian.owl.crow.service;


import xyz.nanian.owl.crow.domain.dto.ChatRequestDTO;
import reactor.core.publisher.Flux;

/**
 * AI
 *
 * @author slnt23
 * @since 2026/4/12
 */

public interface AiChatService {
    /**
     * chat
     *
     * @param dto
     * @return
     */
    String chat(ChatRequestDTO dto);

    /**
     * stream
     *
     * @param dto
     * @return
     */
    Flux<String> chatStream(ChatRequestDTO dto);
}

