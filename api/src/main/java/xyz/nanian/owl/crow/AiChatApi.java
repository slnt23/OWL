package xyz.nanian.owl.crow;


import xyz.nanian.owl.crow.dto.ChatRequestDTO;
import xyz.nanian.owl.result.Result;

/**
 * ai Chat
 *
 * @author slnt23
 * @since 2026/4/12
 */

public interface AiChatApi {

    /**
     * 发送消息
     * @param chatRequestDTO
     * @return
     */
    Result<String> chat(ChatRequestDTO chatRequestDTO);
}
