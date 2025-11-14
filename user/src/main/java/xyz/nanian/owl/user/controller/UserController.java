package xyz.nanian.owl.user.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.user.UserApi;
import xyz.nanian.owl.user.dto.UserDTO;

/**
 * 用户相关的控制器方法
 *
 * @author slnt23
 * @since 2025/11/13
 */

public class UserController implements UserApi {
    private final static Logger log= LoggerFactory.getLogger(UserController.class);

    /**
     * 注册用户
     * @param userDTO 用户DTO
     */
    @Override
    public Result<String> registerUser(UserDTO userDTO) {
        log.info("新用户注册中");

        return Result.success();
    }

    @Override
    public void loginUser(String username, String password) {

    }

    @Override
    public Result<Object> searchUserByName(String name) {
        return null;
    }

    @Override
    public void updateUserByName(String nameId) {

    }
}
