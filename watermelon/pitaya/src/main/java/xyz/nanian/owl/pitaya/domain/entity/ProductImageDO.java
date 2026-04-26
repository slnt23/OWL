package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品图片
 *
 * @author slnt23
 * @since 2026/1/14
 */


@Data
@TableName("product_image")
public class ProductImageDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private String imageUrl;

    private Integer sort;
}
