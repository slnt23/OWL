package xyz.nanian.owl.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@RestController
@RequestMapping("/admin/user-do")
public class UserController {

//    分页获取用户信息，这里只要100个
    public void listUser(){}

//    搜索指定用户
    public void searchUser(){}

//      解封/封禁用户，
    public void updateUserStatus(){}

//    将用户设置为管理员
    public void updateUserIdentityStatus(){}

//   新增用户
    void addUser(String name){};

//    重置密码
    void updateUserPassword(String name){};

}
