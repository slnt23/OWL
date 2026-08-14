package xyz.nanian.owl.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [KEEP] 后台角色管理，保留用于角色列表/新增/更新/删除/启停，后续实现。
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@RestController
@RequestMapping("/api/admin/role-do")
public class RoleAdminController {
    /**
     * 新增用户角色，
     * @param object 角色相关信息，
     */
    void addUserRole(Object object){};

    /**
     * 更新角色信息，
     * @param name 角色名
     */
    void updateUserRole(String name){};

    /**
     * 删除角色
     * @param name 角色名
     */
    void deleteUserRole(String name){};
}
