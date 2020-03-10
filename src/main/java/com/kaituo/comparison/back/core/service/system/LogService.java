package com.kaituo.comparison.back.core.service.system;


import com.kaituo.comparison.back.core.dto.hksdk.Param;
import com.kaituo.comparison.back.core.entity.system.Log;

public interface LogService {

    Log addLog(Param param, String token);

    void update(Log log);

}
