package xyz.nanian.owl.crow.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.crow.domain.dto.ChatRequestDTO;
import xyz.nanian.owl.crow.service.AiChatService;

/**
 * 消息
 *
 * @author slnt23
 * @since 2026/4/12
 */

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI聊天管理",description = "chat")
public class AiChatController {

    private final AiChatService aiChatService;


    @Operation(summary = "用户聊天")
    @PostMapping("/chat")
    public String chat(@RequestBody @Valid ChatRequestDTO dto) {
        return aiChatService.chat(dto);
    }
}

