package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "博客 About 信息 DTO")
public class BlogAboutDTO {

    @Schema(description = "标签行")
    private String tagLine;

    @Schema(description = "个人简介段落数组")
    private List<String> bio;

    @Schema(description = "位置信息")
    private String location;

    @Schema(description = "GitHub 链接")
    private String githubUrl;

    @Schema(description = "CodeTime Badge URL")
    private String codetimeUrl;

    @Schema(description = "诗句/座右铭")
    private String poem;
}