package xyz.nanian.owl.constant;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 返回状态码以及信息
 */

@Getter
public enum ResultStatus {

    // 成功类 (200 系列)
    SUCCESS("操作成功", 200),
    CREATED("创建成功", 201),
    UPDATED("更新成功", 200),
    DELETED("删除成功", 200),

    // 客户端错误 (400 系列)
    BAD_REQUEST("请求参数错误", 400),
    UNAUTHORIZED("暂未登录或TOKEN已经过期", 401),
    FORBIDDEN("没有相关权限", 403),
    NOT_FOUND("资源不存在", 404),
    METHOD_NOT_ALLOWED("请求方法不支持", 405),

    // 服务端错误 (500 系列)
    INTERNAL_SERVER_ERROR("服务器内部错误", 500),
    SERVICE_UNAVAILABLE("服务暂时不可用，请稍后重试", 503),

    // 业务自定义错误 (9000+ 系列)
    BUSINESS_ERROR("业务处理失败", 9000),
    PARAMS_INVALID("参数校验失败", 9001),           // 替换了原有的“上传参数异常”
    DATA_NOT_EXIST("数据不存在", 9002),
    DATA_ALREADY_EXIST("数据已存在", 9003),
    STOCK_INSUFFICIENT("库存不足", 9101),
    ORDER_STATUS_INVALID("订单状态异常，无法执行当前操作", 9201),
    PAYMENT_FAILED("支付失败，请检查账户余额或支付方式", 9301),

    // 特殊状态
    TOKEN_EXPIRED("登录已过期，请重新登录", 401001),
    ACCOUNT_DISABLED("账号已被禁用", 401002),

    // 原有保留项（未出现在新列表中的）
    CONTENT_TYPE_ERR("ContentType错误", 9996),
    API_UN_IMPL("功能尚未实现", 9997),
    SERVER_BUSY("服务器繁忙", 9998),
    FAIL("操作失败", 9999),
    BIZ_ERROR("业务异常",9995);




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
