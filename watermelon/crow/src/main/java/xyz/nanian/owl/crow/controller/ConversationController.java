package xyz.nanian.owl.crow.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.crow.domain.dto.CreateConversationDTO;
import xyz.nanian.owl.crow.service.ConversationService;
import xyz.nanian.owl.crow.domain.vo.ConversationVO;
import xyz.nanian.owl.crow.domain.vo.MessageVO;

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
@Tag(name = "会话管理",description = "会话管理")
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 创建会话
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "新建会话")
    public String createConversation(@RequestBody(required = false) CreateConversationDTO dto) {
        return conversationService.createConversation(dto);
    }

    /**
     * 获取历史会话列表
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "获取会话历史")
    public List<ConversationVO> list() {
        return conversationService.listCurrentUserConversations();
    }

    /**
     * 获取历史消息
     * @param id
     * @return
     */
    @GetMapping("/{id}/messages")
    @Operation(summary = "获取历史消息")
    public List<MessageVO> messages(@PathVariable String id) {
        return conversationService.getMessages(id);
    }

    /**
     * 删除会话
     * @param id
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除会话")
    public void delete(@PathVariable String id) {
        conversationService.deleteConversation(id);
    }
}

