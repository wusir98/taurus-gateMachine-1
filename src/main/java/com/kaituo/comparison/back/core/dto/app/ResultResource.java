package com.kaituo.comparison.back.core.dto.app;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/12 11:31
 * @Modified by:
 */
@Data
public class ResultResource {
    String msg;
    String status;
    List<ResourceData> command;
}
