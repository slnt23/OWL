package xyz.nanian.owl.crow.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.crow.domain.entity.MessageDO;
import xyz.nanian.owl.crow.domain.vo.MessageVO;

import java.util.List;

/**
 * 消息Mapper
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Mapper
public interface MessageMapper extends BaseMapper<MessageDO> {

    List<MessageVO> selectByConversationId(String conversationId);

    List<MessageDO> selectRecentMessages(
            @Param("conversationId") String conversationId,
            @Param("limit") Integer limit
    );
}

