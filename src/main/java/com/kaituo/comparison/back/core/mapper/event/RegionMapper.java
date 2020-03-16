package com.kaituo.comparison.back.core.mapper.event;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaituo.comparison.back.core.entity.event.Region;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RegionMapper extends BaseMapper<Region> {
}
