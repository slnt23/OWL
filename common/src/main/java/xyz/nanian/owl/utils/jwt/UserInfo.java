package xyz.nanian.owl.utils.jwt;


import lombok.Data;

/**
 * 存放线程的用户信息DTO
 *
 * @author slnt23
 * @since 2026/4/10
 */

@Data
public class UserInfo {

    /**
     * 用户id，自增
     */
    public Long userId;

    /**
     * 用户账号编码 UUID
     */
    public String userCode;

    /**
     * 用户email
     */
    public String email;

}
