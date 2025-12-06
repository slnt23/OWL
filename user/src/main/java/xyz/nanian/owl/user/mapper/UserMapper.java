package xyz.nanian.owl.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.user.entity.UserDO;

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
     * 对以上的两个方法的汇总，数据就查询完整的DO，然后交给Service对数据进行处理，
     * 通过手机号来查询用户所有信息
     * @param phone phone
     * @return UserDO
     */
    UserDO select(String phone);

    /**
     * 用户信息更新，通过userCode
     * @param userDO 用户账号
     * @return bool
     */
    Boolean update(UserDO userDO);
}
