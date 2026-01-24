package xyz.nanian.owl.utils.regex;


import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author slnt23
 * @since 2026/1/6
 */


public class RegexUtil {

    /**
     * 正则检验
     * @param regex
     * @param str
     * @return
     */
    public static boolean isMatch(String regex, String str) {
        if (regex == null || str == null) {
            return false;
        }
        return Pattern.matches(regex,str);
    }


    /**
     * 手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone){
        return isMatch(RegexPatterns.PHONE_REGEX,phone);
    }

    /**
     * 邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        return isMatch(RegexPatterns.EMAIL_REGEX,email);
    }

    /**
     * 密码
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        return isMatch(RegexPatterns.PASSWORD_REGEX,password);
    }

    /**
     * 验证码
     * @param verifyCode
     * @return
     */
    public static boolean isVerifyCode(String verifyCode){
        return isMatch(RegexPatterns.VERIFY_CODE_REGEX,verifyCode);
    }
}
