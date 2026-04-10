package xyz.nanian.owl.constant;


/**
 * 用户信息默认值
 *
 * @author slnt23
 * @since 2026/4/10
 */

public class UserConstant {

    /**
     * 新用户默认用户名
     * 邮箱注册时，用户无需手动设置用户名，自动填充该默认值
     * 后续可在个人中心修改
     */
    public static final String DEFAULT_USER_NAME = "用户";

    /**
     * 新用户默认密码（仅作占位，邮箱注册无需密码，该字段可废弃）
     * 若后续保留密码登录，可作为初始默认密码；纯邮箱登录可删除该常量
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 新用户默认头像URL
     * 邮箱注册时，自动填充默认头像，用户可后续在个人中心更换
     * 实际使用时替换为真实的默认头像CDN地址
     */
    public static final String DEFAULT_AVATAR = "默认头像URL";

    /**
     * 新用户默认角色
     * 0 = 普通消费者用户（对应数据库角色定义：0=用户，1=商家，2=管理员）
     * 邮箱注册的新用户默认分配普通用户权限，商家/管理员需后台手动分配
     */
    public static final Integer DEFAULT_ROLE = 0;

    /**
     * 新用户默认账号状态
     * 1 = 正常（对应数据库状态定义：0=正常，1=封禁，此处需注意：
     * 若按你数据库定义「0=正常，1=封禁」，建议将默认值改为0，避免新用户直接封禁）
     * 邮箱验证通过后，账号默认正常可用，无需手动激活
     */
    public static final Integer DEFAULT_STATUS = 0;

}
