package com.kaituo.comparison.back.core.service.event;

import com.alibaba.fastjson.JSONArray;

public interface EventService {

    void handle(JSONArray list);
}
