package com.kaituo.comparison.back.core.mapper.event;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaituo.comparison.back.core.entity.event.Door;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DoorMapper extends BaseMapper<Door> {
    String getPidById(@Param("id") String id);

    List<Door> getAuthDoor(@Param("areaId") String areaId
            ,@Param("unitNo") String unitNo);
}
