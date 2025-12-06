package xyz.nanian.owl.user.service;


import xyz.nanian.owl.user.dto.UserInfoDTO;
import xyz.nanian.owl.user.dto.UserRegisterDTO;

/**
 * 用户相关的逻辑方法接口
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface UserService {

    /**
     * 注册新用户
     * @param userRegisterDTO 用户DTO基本信息
     */
    void saveUser(UserRegisterDTO userRegisterDTO);

    /**
     * 登陆验证
     * @param phone 手机号
     * @param password 输入的初始密码
     * @return 密码是否正确的 bool
     */
    Boolean login(String phone, String password);

    /**
     * 更新用户信息
     * @param userInfoDTO 用户DTO
     * @return 是否更改成功bool
     */
    Boolean updateUserInfo(UserInfoDTO userInfoDTO);

    /**
     * 更改用户密码
     * @param phone 原手机号
     * @param newPassword 新密码
     * @return message
     */
    Boolean updateUserPassword(String phone,String newPassword);
}
