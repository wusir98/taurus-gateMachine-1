package com.kaituo.comparison.back.core.service.hksdk;

import com.kaituo.comparison.back.core.dto.app.PeoPleData;
import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/13 12:15
 * @Modified by:
 */

public interface HkAuthService {

    void addAuth(ResultRegister resultRegister);

    void qr(List<PeoPleData> listPeoPleData);
    void tempAuth(String personId,String areaid,String unitno);
    String startTask();
}
