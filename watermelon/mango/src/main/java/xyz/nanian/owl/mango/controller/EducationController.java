package xyz.nanian.owl.mango.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.mango.domain.dto.EducationDTO;
import xyz.nanian.owl.mango.domain.vo.EducationVO;
import xyz.nanian.owl.mango.service.EducationService;

import java.util.List;

/**
 * 博客教育经历：公开读 + 登录写
 *
 * @author slnt23
 * @since 2026/8/22
 */
@RestController
@RequestMapping("/api/blog/education")
@RequiredArgsConstructor
@Tag(name = "博客教育经历")
public class EducationController {

    private final EducationService educationService;

    /**
     * 全部教育经历
     */
    @GetMapping
    @Operation(summary = "全部教育经历")
    public Result<List<EducationVO>> list() {
        return Result.success(educationService.list());
    }

    /**
     * 新增教育经历
     */
    @PostMapping
    @Operation(summary = "新增教育经历")
    public Result<Long> create(@Valid @RequestBody EducationDTO dto) {
        return Result.success(educationService.create(dto));
    }

    /**
     * 更新教育经历
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新教育经历")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody EducationDTO dto) {
        return educationService.update(id, dto) ? Result.success() : Result.fail();
    }

    /**
     * 删除教育经历
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除教育经历")
    public Result<Void> delete(@PathVariable Long id) {
        return educationService.deleteById(id) ? Result.success() : Result.fail();
    }
}
