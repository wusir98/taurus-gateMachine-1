package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/13 18:22
 * @Modified by:
 */
@Data
public class TaskProgressReturn {
    String code;
    String msg;
    TaskProgress data;
}
