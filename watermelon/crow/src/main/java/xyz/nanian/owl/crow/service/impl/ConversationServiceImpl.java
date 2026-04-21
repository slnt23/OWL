package xyz.nanian.owl.crow.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.crow.domain.dto.CreateConversationDTO;
import xyz.nanian.owl.crow.domain.entity.ConversationDO;
import xyz.nanian.owl.crow.mapper.ConversationMapper;
import xyz.nanian.owl.crow.mapper.MessageMapper;
import xyz.nanian.owl.crow.mapstruct.ConversationConvert;
import xyz.nanian.owl.crow.service.ConversationService;
import xyz.nanian.owl.crow.domain.vo.ConversationVO;
import xyz.nanian.owl.crow.domain.vo.MessageVO;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.time.LocalDateTime;
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
    private final ConversationConvert conversationConvert;

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
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setUpdatedAt(LocalDateTime.now());
//        获取用户账号，
        conversation.setUserCode(UserContext.getUserCode());

        log.info("conversationId:{}",conversation);

        conversationMapper.insert(conversation);

        return conversationId;
    }

    /**
     * 获取当前用户的会话列表
     * @return
     */
    @Override
    public List<ConversationVO> listCurrentUserConversations() {

        String userCodeId = UserContext.getUserCode();

        LambdaQueryWrapper<ConversationDO> wrapper = new LambdaQueryWrapper<ConversationDO>();
        wrapper.eq(ConversationDO::getUserCode,userCodeId);

//        查询当前用户的会话列表
        List<ConversationDO> conversationDOS= conversationMapper.selectList(wrapper);

        return conversationConvert.conversationDOListToConversationVOList(conversationDOS);

//        下面是手动转换的代码，使用mapstruct后就不需要了
//        return conversationDOS.stream()
//                .map(conversationDO -> {
//                    ConversationVO vo = new ConversationVO();
//                    vo.setId(conversationDO.getId());
//                    vo.setTitle(conversationDO.getTitle());
//                    return vo;
//                })
//                .toList();
    }

    /**
     * 获取会话消息列表
     * @param conversationId
     * @return
     */
    @Override
    public List<MessageVO> getMessages(String conversationId) {
        return messageMapper.selectByConversationId(conversationId);
    }

    /**
     * 删除会话
     * @param conversationId
     */
    @Override
    public void deleteConversation(String conversationId) {
        conversationMapper.deleteById(conversationId);
    }
}

