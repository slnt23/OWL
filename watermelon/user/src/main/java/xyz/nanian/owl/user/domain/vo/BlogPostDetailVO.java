package xyz.nanian.owl.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "博客文章详情 VO")
public class BlogPostDetailVO extends BlogPostVO {
}