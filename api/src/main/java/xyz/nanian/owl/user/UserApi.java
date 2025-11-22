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
     * 搜索用户通过用户名
     * TODO 这个暂时可以不做，这似乎是社交软件才用的，
     * @param name 用户名
     * @return 搜索用户相关信息
     */
    Result<Object> searchUserByName(String name);

    /**
     * 更新用户信息
     * @param userInfoDTO 用户最新信息
     * @return message
     */
    Result<Object> updateUser(UserInfoDTO userInfoDTO);

    /**
     * 更新用户头像通过手机号，
     * TODO 文件的OSS 或者fastDFS,但是有的说都用OSS，或者fastDFS过时了，难说，
     * @param phone phone
     * @return message
     */
    Result<String> updateAvatarByCode(String phone);

    /**
     * 更新用户密码通过手机号
     * @param phone phone
     * @param newPassword newPassword
     * @return message
     */
    Result<String> updatePasswordByCode(String phone,String newPassword);

}
