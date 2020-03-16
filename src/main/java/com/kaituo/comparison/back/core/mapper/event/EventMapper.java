package com.kaituo.comparison.back.core.mapper.event;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaituo.comparison.back.core.entity.event.Event;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository
public interface EventMapper extends BaseMapper<Event> {

    Date countMaxEventTime();
}
