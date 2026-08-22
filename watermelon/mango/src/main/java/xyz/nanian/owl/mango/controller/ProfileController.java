package xyz.nanian.owl.mango.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.mango.domain.dto.ProfileUpdateDTO;
import xyz.nanian.owl.mango.domain.vo.ProfileVO;
import xyz.nanian.owl.mango.service.ProfileService;

/**
 * 博客站长个人信息：公开读 + 登录写
 *
 * @author slnt23
 * @since 2026/8/22
 */
@RestController
@RequestMapping("/api/blog/profile")
@RequiredArgsConstructor
@Tag(name = "博客个人信息")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 获取站长信息
     */
    @GetMapping
    @Operation(summary = "获取站长信息")
    public Result<ProfileVO> get() {
        return Result.success(profileService.get());
    }

    /**
     * 更新站长信息
     */
    @PutMapping
    @Operation(summary = "更新站长信息")
    public Result<Void> update(@RequestBody ProfileUpdateDTO dto) {
        return profileService.update(dto) ? Result.success() : Result.fail();
    }
}
