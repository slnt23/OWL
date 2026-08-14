package xyz.nanian.owl.user.constant;


/**
 * 用户信息默认值
 *
 * @author slnt23
 * @since 2026/4/10
 */

public class UserConstant {

    /**
     * 新用户默认用户名
     */
    public static final String DEFAULT_USER_NAME = "User";

    // [TO_BE_DELETED] 默认密码已废弃，只有用户更改密码后才可以用密码登陆。
    // public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 新用户默认头像URL
     * 实际使用时替换为真实地默认头像CDN地址
     */
    public static final String DEFAULT_AVATAR = "/DEFAULT_AVATAR.png";

    /**
     * 新用户默认角色 0 = 普通消费者用户（对应数据库角色定义：1=用户，）
     * 具体role信息查表
     */
    public static final Long DEFAULT_ROLE = 1L;

    /**
     * 新用户默认账号状态
     * 1 = 正常（对应数据库状态定义：0=正常，1=封禁，此处需注意：
     * 邮箱验证通过后，账号默认正常可用，无需手动激活
     */
    public static final Integer DEFAULT_STATUS = 0;

    public static final String DEFAULT_NICK_NAME = "默认昵称";
    public static final String DEFAULT_REMARK = "备注，写点什么好呢？";

}
