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
import xyz.nanian.owl.common.result.Result;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

/**
 * 消息
 *
 * @author slnt23
 * @since 2026/4/12
 */

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI聊天管理", description = "chat")
public class AiChatController {

    private final AiChatService aiChatService;

    //    非流式输出
    @Operation(summary = "用户聊天")
    @PostMapping("/chat")
    public Result<String> chat(@RequestBody @Valid ChatRequestDTO dto) {
        return Result.success(aiChatService.chat(dto));
    }

    //    流式输出回复，
    @Operation(summary = "流式聊天")
    @PostMapping(
            value = "/chat/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> chatStream(
            @RequestBody @Valid ChatRequestDTO dto) {

        return aiChatService.chatStream(dto);
    }


}

