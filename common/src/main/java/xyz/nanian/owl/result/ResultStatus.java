package xyz.nanian.owl.result;


import lombok.Getter;

/**
 * 返回状态码以及信息
 */

@Getter
public enum ResultStatus {

    UNAUTHORIZED("暂未登录或TOKEN已经过期", 401),
    NOT_FOUND("资源不存在",404),
    FORBIDDEN("没有相关权限", 403),
    SERVER_ERROR("服务器错误", 9994),
    PARAMS_INVALID("上传参数异常", 9995),
    CONTENT_TYPE_ERR("ContentType错误", 9996),
    API_UN_IMPL("功能尚未实现", 9997),
    SERVER_BUSY("服务器繁忙", 9998),
    FAIL("操作失败", 9999),
    SUCCESS("操作成功");



    private final String message;
    private final Integer code;

    ResultStatus(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    ResultStatus(String message) {
        this(message,10000);
    }

}
