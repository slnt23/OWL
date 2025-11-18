package xyz.nanian.owl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页统一包装类，
 *
 * @author slnt23
 * @since 2025/11/18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "分页统一包装")
public class PageResult<T> {

    @Schema(description = "当前页码")
    private Long currentPage;

    @Schema(description = "每页条数")
    private Long pageSize;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Long totalPage;

    @Schema(description = "数据列表")
    private List<T> records;

    /**
     * 将分页插件所包装的Page 转换为PageResult
     * @param page IPage
     * @return 返回的分页数据
     * @param <T> 类表元素模型模板一般为DO
     */
    public static <T> PageResult<T> create(IPage<T> page){
        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }



}
