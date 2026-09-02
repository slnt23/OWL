// [MOVED_TO_USER_MODULE] 博客技能接口已迁移至 user 模块 BlogSettingsController
// package xyz.nanian.owl.mango.controller;
//
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;
// import xyz.nanian.owl.common.result.Result;
// import xyz.nanian.owl.mango.domain.dto.SkillCategoryDTO;
// import xyz.nanian.owl.mango.domain.dto.SkillItemDTO;
// import xyz.nanian.owl.mango.domain.vo.SkillCategoryVO;
// import xyz.nanian.owl.mango.service.SkillService;
// import java.util.List;
//
// @RestController
// @RequestMapping("/api/blog/skills")
// @RequiredArgsConstructor
// @Tag(name = "博客技能")
// public class SkillController {
//
//     private final SkillService skillService;
//
//     @GetMapping
//     @Operation(summary = "全部技能（含条目）")
//     public Result<List<SkillCategoryVO>> list() {
//         return Result.success(skillService.list());
//     }
//
//     @PostMapping("/categories")
//     @Operation(summary = "新增技能分类")
//     public Result<Long> createCategory(@Valid @RequestBody SkillCategoryDTO dto) {
//         return Result.success(skillService.createCategory(dto));
//     }
//
//     @PutMapping("/categories/{id}")
//     @Operation(summary = "更新技能分类")
//     public Result<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody SkillCategoryDTO dto) {
//         return skillService.updateCategory(id, dto) ? Result.success() : Result.fail();
//     }
//
//     @DeleteMapping("/categories/{id}")
//     @Operation(summary = "删除技能分类")
//     public Result<Void> deleteCategory(@PathVariable Long id) {
//         return skillService.deleteCategory(id) ? Result.success() : Result.fail();
//     }
//
//     @PostMapping("/items")
//     @Operation(summary = "新增技能条目")
//     public Result<Long> createItem(@Valid @RequestBody SkillItemDTO dto) {
//         return Result.success(skillService.createItem(dto));
//     }
//
//     @PutMapping("/items/{id}")
//     @Operation(summary = "更新技能条目")
//     public Result<Void> updateItem(@PathVariable Long id, @Valid @RequestBody SkillItemDTO dto) {
//         return skillService.updateItem(id, dto) ? Result.success() : Result.fail();
//     }
//
//     @DeleteMapping("/items/{id}")
//     @Operation(summary = "删除技能条目")
//     public Result<Void> deleteItem(@PathVariable Long id) {
//         return skillService.deleteItem(id) ? Result.success() : Result.fail();
//     }
// }