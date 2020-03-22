package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/11 19:06
 * @Modified by:
 */
@Data
public class AuthAdd {
    List<PersonDatas> personDatas;
    Set<ResourceInfo> resourceInfos;
    String startTime;
    String endTime;
}
