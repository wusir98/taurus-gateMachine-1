package com.kaituo.comparison.back.core.service.system;


import com.kaituo.comparison.back.core.dto.hksdk.Param;
import com.kaituo.comparison.back.core.entity.system.Log;
import com.kaituo.comparison.back.core.mapper.system.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;

public interface LogService {

    String addLog(Param param, String token);
    void update(String id,String result,String msg);

}
