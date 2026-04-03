package xyz.nanian.owl.admin;


/**
 * 用户管理
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface UserManageApi {


    /**
     * 新增用户 TODO 暂时可以推迟
     * @param name 用户名
     */
    void addUser(String name);

    /**
     * 删除用户
     * @param name 用户名
     */
    void deleteUserByName(String name);

    /**
     * 更新用户状态，封号
     * @param name 用户名
     */
    void updateUserStatus(String name);

    /**
     * 重置用户密码为123456
     * @param name 用户名
     */
    void updateUserPassword(String name);


}

