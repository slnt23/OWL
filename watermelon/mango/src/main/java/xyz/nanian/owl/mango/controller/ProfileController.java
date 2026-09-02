// [MOVED_TO_USER_MODULE] 博客个人信息接口已迁移至 user 模块 BlogSettingsController
// package xyz.nanian.owl.mango.controller;
//
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import xyz.nanian.owl.common.result.Result;
// import xyz.nanian.owl.mango.domain.dto.ProfileUpdateDTO;
// import xyz.nanian.owl.mango.domain.vo.ProfileVO;
// import xyz.nanian.owl.mango.service.ProfileService;
//
// @RestController
// @RequestMapping("/api/blog/profile")
// @RequiredArgsConstructor
// @Tag(name = "博客个人信息")
// public class ProfileController {
//
//     private final ProfileService profileService;
//
//     @GetMapping
//     @Operation(summary = "获取站长信息")
//     public Result<ProfileVO> get() {
//         return Result.success(profileService.get());
//     }
//
//     @PutMapping
//     @Operation(summary = "更新站长信息")
//     public Result<Void> update(@RequestBody ProfileUpdateDTO dto) {
//         return profileService.update(dto) ? Result.success() : Result.fail();
//     }
// }