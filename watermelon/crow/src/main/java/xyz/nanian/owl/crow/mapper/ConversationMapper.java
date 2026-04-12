package xyz.nanian.owl.crow.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.crow.domain.entity.ConversationDO;
import xyz.nanian.owl.crow.domain.vo.ConversationVO;

import java.util.List;

/**
 * 会话管理
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Mapper
public interface ConversationMapper extends BaseMapper<ConversationDO> {

    List<ConversationVO> selectByUserId(String userId);
}

