package xyz.nanian.owl.common.domain.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页用的，
 *
 * @author slnt23
 * @since 2026/7/30
 */

@Getter
@Setter
@ToString
public class PageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
