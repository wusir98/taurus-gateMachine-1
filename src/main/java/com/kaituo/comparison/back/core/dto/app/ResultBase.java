package com.kaituo.comparison.back.core.dto.app;

import lombok.Data;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/12 15:06
 * @Modified by:
 */
@Data
public class ResultBase<T> {
    String msg;
    String status;
    T command;
}
