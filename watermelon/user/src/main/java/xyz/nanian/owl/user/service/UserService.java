package xyz.nanian.owl.user.service;


import org.springframework.web.multipart.MultipartFile;
import xyz.nanian.owl.user.domain.dto.EmailBindDTO;
import xyz.nanian.owl.user.domain.dto.PasswordUpdateDTO;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.domain.dto.UserInfoUpdateDTO;
import xyz.nanian.owl.user.domain.vo.UserInfoVO;

/**
 * 用户相关的逻辑方法接口
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface UserService {


    // [TO_BE_DELETED] 旧资料更新接口，请使用 updateUserInfo(UserInfoUpdateDTO)。
    // @Deprecated
    // Boolean updateUserInfo(UserInfoDTO userInfoDTO);

    // [TO_BE_DELETED] 旧改密接口，请使用 updateUserPassword(PasswordUpdateDTO)。
    // @Deprecated
    // Boolean updateUserPassword(String newPassword);

    /**
     * 更新用户头像
     * @param file
     * @param userCode
     * @return
     */
    String updateUserAvatar(MultipartFile file, String userCode);

    /**
     * 获取用户信息，
     * @return
     */
    UserInfoVO getUserInfoByCode();

    /**
     * [UPGRADE] 更新用户资料，仅允许 userName/nickname/phone/remark。
     */
    Boolean updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO);

    /**
     * [UPGRADE] 换绑邮箱，需要新邮箱验证码。
     */
    Boolean updateUserEmail(EmailBindDTO emailBindDTO);

    /**
     * [UPGRADE] 登录后修改密码，旧密码校验由服务层完成。
     */
    Boolean updateUserPassword(PasswordUpdateDTO passwordUpdateDTO);
}
