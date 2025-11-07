package xyz.nanian.owl.result;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回包装类
 *
 * @author slnt23
 * @since 2025/11/8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "统一返回包装类")
public class Result<T> {

//    状态码
    @ApiModelProperty(value = "状态码",example = "10000")
    private Integer code;

//    返回提示信息
    @ApiModelProperty(value = "提示信息",example = "发送信息成功")
    private String message;

//    返回数据
    @ApiModelProperty(value = "data",example = "数据")
    private T data;


    /**
     * 设置状态码和提示信息
     * @param resultStatus 结果状态枚举
     */
    private void SetStatus(ResultStatus resultStatus) {
        this.setCode(resultStatus.getCode());
        this.setMessage(resultStatus.getMessage());
    }

    /**
     * 通过枚举创建返回类型
     * @param data 返回
     * @param resultStatus 状态信息
     * @return 返回值为包装好的Result类对象
     * @param <T> data数据类型
     */
    public static <T> Result<T> create(T data ,ResultStatus resultStatus) {
        Result<T> result = new Result<>();
        result.setCode(resultStatus.getCode());
        result.setMessage(resultStatus.getMessage());
        result.setData(data);
        return result;
    }


    /**
     * 成功、有返回数据
     * @param data 返回数据类型
     * @param resultStatus 状态码
     * @return 返回包装好的Result类数据
     * @param <T> data数据类型
     */
    public static <T> Result<T> success(T data ,ResultStatus resultStatus) {
        return create(data,resultStatus);
    }


    /**
     * 成功、无返回数据
     * @return 返回状态提示，
     * @param <T> null
     */
    public static <T> Result<T> success() {
        return create(null,ResultStatus.SUCCESS);
    }

    /**
     * 失败、有返回数据
     * @param data 返回数据
     * @param resultStatus 状态码
     * @return 有返回数据data
     * @param <T> data类型
     */
    public static <T> Result<T> fail(T data ,ResultStatus resultStatus) {
        return create(data,resultStatus);
    }


    /**
     * 失败、无返回数据
     * @return 无返回data
     * @param <T> null
     */
    public static <T> Result<T> fail(){
        return create(null,ResultStatus.FAIL);
    }

}
