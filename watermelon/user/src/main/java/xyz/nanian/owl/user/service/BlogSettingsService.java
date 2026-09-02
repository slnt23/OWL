package xyz.nanian.owl.user.service;

import xyz.nanian.owl.user.domain.dto.BlogSettingsDTO;
import xyz.nanian.owl.user.domain.vo.BlogSettingsVO;

public interface BlogSettingsService {

    BlogSettingsVO get(Long userId);

    Boolean update(BlogSettingsDTO dto, Long userId);
}