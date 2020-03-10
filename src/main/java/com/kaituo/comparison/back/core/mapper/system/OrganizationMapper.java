package com.kaituo.comparison.back.core.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrganizationMapper extends BaseMapper<com.kaituo.comparison.back.core.entity.system.Organization> {
}
