package xyz.nanian.owl.user;


import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.dto.UserDTO;

/**
 * 用户模块对外接口
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface UserApi {


    /**
     * 用户注册
     * @param userDTO 用户DTO
     */
    void registerUser(UserDTO userDTO);

    /**
     * 用户登陆
     * @param username 用户名
     * @param password 用户密码
     */
    void loginUser(String username, String password);

    /**
     * 搜索用户通过用户名
     * @param name 用户名
     * @return 搜索用户相关信息
     */
    Result<Object> searchUserByName(String name);

    /**
     * 更新用户信息
     * @param nameId 用户名
     */
    void updateUserByName(String nameId);



}
