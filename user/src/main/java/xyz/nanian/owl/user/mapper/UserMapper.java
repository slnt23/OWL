package xyz.nanian.owl.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
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
     * 通过手机号找到对应账号的password哈希值
     * @param phone 账号手机号
     * @return password 哈希值
     */
    String selectPasswordByPhone(String phone);
}
