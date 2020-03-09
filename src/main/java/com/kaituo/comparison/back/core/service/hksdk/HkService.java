package com.kaituo.comparison.back.core.service.hksdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.kaituo.comparison.back.common.bean.ResponseResult2;
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
@Service
public class HkService {
    @Value("${hk.host}")
    String host;
    @Value("${hk.appKey}")
    String appKey;
    @Value("${hk.appSecret}")
    String appSecret;

    @Autowired
    RestTemplate restTemplate;

    public <T> ResponseResult2<T> getResponse(String uri, JSON param,Class<T> responseType){
        return new ResponseResult2<T>();
    }

    public  String getResponse(String uri, String param){
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        ArtemisConfig.host = host; // artemis网关服务器ip端口
        ArtemisConfig.appKey = appKey;  // 秘钥appkey
        ArtemisConfig.appSecret = appSecret;// 秘钥appSecret

        /**
         * STEP2：设置OpenAPI接口的上下文
         */
        final String ARTEMIS_PATH = "/artemis";

        /**
         * STEP3：设置接口的URI地址
         */
        final String previewURLsApi = ARTEMIS_PATH + uri;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        String body = param;
        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);
        return result;
    }

}
