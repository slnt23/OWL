package xyz.nanian.owl.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
// [TO_BE_DELETED] import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.user.domain.entity.UserDO;

/**
 * 用户数据Mapper
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {


    // [TO_BE_DELETED] 空实现，用户搜索后续交给 administration 模块。
    // @Deprecated
    // IPage<UserDO> selectUserByName(String name);

}
