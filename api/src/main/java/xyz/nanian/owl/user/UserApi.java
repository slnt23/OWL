package xyz.nanian.owl.user;


import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.dto.UserInfoDTO;
import xyz.nanian.owl.user.dto.UserRegisterDTO;

/**
 * 用户模块对外接口
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface UserApi {


    /**
     * 用户注册
     * @param user 用户DTO
     */
    Result<String> registerUser(UserRegisterDTO user);

    /**
     * 用户登陆
     * TODO 了解网关，JWT等登陆 相关的模块再添加新内容或者重构 ,JWT,Session等可以做登陆状态维持
     * @param username 用户名
     * @param password 用户密码
     */
    Result<String> loginUser(String username, String password);

    /**
     * 搜索用户通过用户名 TODO 这个暂时可以不做，这似乎是社交软件才用的，
     * @param name 用户名
     * @return 搜索用户相关信息
     */
    Result<Object> searchUserByName(String name);

    /**
     * 更新用户信息
     * @param userInfoDTO 用户最新信息
     * @param rawPhone 原手机号，或者正在使用的
     * @return message
     */
    Result<Object> updateUserByName(UserInfoDTO userInfoDTO, String rawPhone);

    /**
     * 更新用户头像通过手机号，TODO 文件的
     * @param Phone phone
     * @return message
     */
    Result<String> updateAvatarByCode(String Phone);

    /**
     * 更新用户密码通过手机号
     * @param Phone phone
     * @return message
     */
    Result<String> updatePasswordByCode(String Phone);

}
