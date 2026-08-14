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
     * 更新用户头像，上传到 MinIO 并删除旧头像。
     *
     * @param file     头像文件
     * @param userCode 用户账号编号
     * @return 新头像 URL
     */
    String updateUserAvatar(MultipartFile file, String userCode);

    /**
     * 获取当前登录用户的信息，包含角色名称和可访问的头像 URL。
     *
     * @return 用户信息 VO
     */
    UserInfoVO getUserInfoByCode();

    /**
     * 更新当前用户资料，仅允许修改用户名、昵称、手机号和备注。
     *
     * @param userInfoUpdateDTO 待更新字段
     * @return true 表示更新成功
     */
    Boolean updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO);

    /**
     * 换绑邮箱，需要新邮箱验证码，成功后旧 token 失效。
     *
     * @param emailBindDTO 新邮箱和验证码
     * @return true 表示换绑成功
     */
    Boolean updateUserEmail(EmailBindDTO emailBindDTO);

    /**
     * 登录后修改密码，已设置过密码时必须校验旧密码，成功后旧 token 失效。
     *
     * @param passwordUpdateDTO 旧密码和新密码
     * @return true 表示修改成功
     */
    Boolean updateUserPassword(PasswordUpdateDTO passwordUpdateDTO);
}
