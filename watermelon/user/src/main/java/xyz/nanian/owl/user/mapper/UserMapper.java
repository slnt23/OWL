package xyz.nanian.owl.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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


    /**
     * 分页查询用户，用户名可能有多个重复的，
     * @param name 用户名
     * @return 用户的分页数据
     */
    IPage<UserDO> selectUserByName(String name);

    /**
     * 用户信息更新，通过userCode
     * 只要是UserDO中有的，都可以更新，不需要单独的接口来更新某个字段，注意参数，
     *
     * @param userDO 用户账号
     * @return bool
     */
    int update(UserDO userDO);

}
