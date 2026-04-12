package xyz.nanian.owl.crow.service;


import xyz.nanian.owl.crow.domain.dto.ChatRequestDTO;

/**
 * AI
 *
 * @author slnt23
 * @since 2026/4/12
 */

public interface AiChatService {

    String chat(ChatRequestDTO dto);
}

