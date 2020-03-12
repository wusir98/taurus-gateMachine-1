package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/11 19:05
 * @Modified by:
 */
@Data
public class PersonDatas {
    String personDataType = "person";
    List<String> indexCodes;
}
