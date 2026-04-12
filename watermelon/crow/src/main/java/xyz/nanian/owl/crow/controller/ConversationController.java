package xyz.nanian.owl.crow.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.crow.dto.CreateConversationDTO;
import xyz.nanian.owl.crow.service.ConversationService;
import xyz.nanian.owl.crow.vo.ConversationVO;
import xyz.nanian.owl.crow.vo.MessageVO;

import java.util.List;

/**
 * 会话管理
 *
 * @author slnt23
 * @since 2026/4/12
 */

@RestController
@RequestMapping("/ai/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public String createConversation(
            @RequestBody(required = false) CreateConversationDTO dto
    ) {
        return conversationService.createConversation(dto);
    }

    @GetMapping("/list")
    public List<ConversationVO> list() {
        return conversationService.listCurrentUserConversations();
    }

    @GetMapping("/{id}/messages")
    public List<MessageVO> messages(@PathVariable String id) {
        return conversationService.getMessages(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        conversationService.deleteConversation(id);
    }
}

