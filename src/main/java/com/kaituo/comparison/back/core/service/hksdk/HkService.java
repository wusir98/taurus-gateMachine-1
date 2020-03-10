package com.kaituo.comparison.back.core.service.hksdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.kaituo.comparison.back.common.bean.ResponseResult2;
import com.kaituo.comparison.back.core.dto.hksdk.RestToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/9 9:58
 * @Modified by:
 */
public interface HkService {

    String getResponse(String uri, Object param);
    boolean isAllow(String uri,String token);
    void updateToken(RestToken restToken);
}
