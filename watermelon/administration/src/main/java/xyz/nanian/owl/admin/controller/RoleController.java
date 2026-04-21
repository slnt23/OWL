package xyz.nanian.owl.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@RestController
@RequestMapping("/admin/role-do")
public class RoleController {
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
