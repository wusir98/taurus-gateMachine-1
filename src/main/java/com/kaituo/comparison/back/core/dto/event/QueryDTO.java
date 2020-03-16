package com.kaituo.comparison.back.core.dto.event;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class QueryDTO implements Serializable {
    private String uri;
    private JSONObject param;
}
