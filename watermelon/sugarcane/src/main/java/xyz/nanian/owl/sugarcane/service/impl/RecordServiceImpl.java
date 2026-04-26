package xyz.nanian.owl.sugarcane.service.impl;

import lombok.RequiredArgsConstructor;
import xyz.nanian.owl.sugarcane.domain.dto.PriceLatestQueryDTO;
import xyz.nanian.owl.sugarcane.domain.entity.RecordDO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceLatestVO;
import xyz.nanian.owl.sugarcane.mapper.RecordMapper;
import xyz.nanian.owl.sugarcane.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 价格记录表（时间序列数据） 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {

    final private RecordMapper recordMapper;

    @Override
    public PriceLatestVO queryLatest(PriceLatestQueryDTO dto) {

        return null;
    }
}
