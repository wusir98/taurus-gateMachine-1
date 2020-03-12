package com.kaituo.comparison.back.core.dto.app;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/12 9:50
 * @Modified by:
 */
@Data
public class ResultRegister {
    String msg;
    String status;
    List<PeoPleData> command;
}
