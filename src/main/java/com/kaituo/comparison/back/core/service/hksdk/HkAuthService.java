package com.kaituo.comparison.back.core.service.hksdk;

import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/13 12:15
 * @Modified by:
 */

public interface HkAuthService {
    void addAuth(ResultRegister resultRegister, ResultResource resultResource);

    void modifyAuth(ResultRegister resultRegister, ResultResource resultResource);

    void deleteAuth(ResultRegister resultRegister, ResultResource resultResource);
}
