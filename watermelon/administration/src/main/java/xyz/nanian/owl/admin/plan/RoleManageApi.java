package xyz.nanian.owl.admin.plan;


/**
 * 角色相关的对外接口
 *
 * @author slnt23
 * @since 2025/11/12
 */

public interface RoleManageApi {

    /**
     * 新增用户角色，
     * @param object 角色相关信息，
     */
    void addUserRole(Object object);

    /**
     * 更新角色信息，
     * @param name 角色名
     */
    void updateUserRole(String name);

    /**
     * 删除角色
     * @param name 角色名
     */
    void deleteUserRole(String name);
}
