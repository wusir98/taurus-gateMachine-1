package com.kaituo.comparison.back.core.service.hksdk;

import com.kaituo.comparison.back.core.dto.hksdk.RestToken;

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

    void startHkAync();
}
