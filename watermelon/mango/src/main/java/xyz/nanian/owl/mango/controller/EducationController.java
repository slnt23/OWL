// [MOVED_TO_USER_MODULE] 博客教育经历接口已迁移至 user 模块 BlogSettingsController
// package xyz.nanian.owl.mango.controller;
//
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;
// import xyz.nanian.owl.common.result.Result;
// import xyz.nanian.owl.mango.domain.dto.EducationDTO;
// import xyz.nanian.owl.mango.domain.vo.EducationVO;
// import xyz.nanian.owl.mango.service.EducationService;
// import java.util.List;
//
// @RestController
// @RequestMapping("/api/blog/education")
// @RequiredArgsConstructor
// @Tag(name = "博客教育经历")
// public class EducationController {
//
//     private final EducationService educationService;
//
//     @GetMapping
//     @Operation(summary = "全部教育经历")
//     public Result<List<EducationVO>> list() {
//         return Result.success(educationService.list());
//     }
//
//     @PostMapping
//     @Operation(summary = "新增教育经历")
//     public Result<Long> create(@Valid @RequestBody EducationDTO dto) {
//         return Result.success(educationService.create(dto));
//     }
//
//     @PutMapping("/{id}")
//     @Operation(summary = "更新教育经历")
//     public Result<Void> update(@PathVariable Long id, @Valid @RequestBody EducationDTO dto) {
//         return educationService.update(id, dto) ? Result.success() : Result.fail();
//     }
//
//     @DeleteMapping("/{id}")
//     @Operation(summary = "删除教育经历")
//     public Result<Void> delete(@PathVariable Long id) {
//         return educationService.deleteById(id) ? Result.success() : Result.fail();
//     }
// }