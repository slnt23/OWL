package xyz.nanian.owl.crow.mapstruct;


import org.mapstruct.Mapper;
import xyz.nanian.owl.crow.domain.entity.ConversationDO;
import xyz.nanian.owl.crow.domain.vo.ConversationVO;

import java.util.List;

/**
 * 会话类型转化
 *
 * @author slnt23
 * @since 2026/4/18
 */

@Mapper(componentModel = "spring")
public interface ConversationConvert {

    List<ConversationVO> conversationDOListToConversationVOList(List<ConversationDO> conversationDOS);
    ConversationVO conversationDOToConversationVO(ConversationDO conversationDO);

}
