package xyz.nanian.owl.pitaya.controller;


import xyz.nanian.owl.result.Result;

/**
 * 用户模块对外接口
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface UserApi {

    /**
     * 搜索用户通过用户名
     * @param name 用户名
     * @return 搜索用户相关信息
     */
    Result<Object> searchUserByName(String name);

    /**
     * 删除用户
     * @param name 用户名
     */
    void deleteUserByName(String name);

    /**
     * 更新用户信息
     * @param name 用户名
     */
    void updateUserByName(String name);

    /**
     * 新增用户
     * @param name 用户名
     */
    void addUser(String name);




}
