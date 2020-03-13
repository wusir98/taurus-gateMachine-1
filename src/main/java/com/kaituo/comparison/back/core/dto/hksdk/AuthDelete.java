package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/13 11:04
 * @Modified by:
 */
@Data
public class AuthDelete {
    List<PersonDatas> personDatas;
    List<ResourceInfo> resourceInfos;
}
