package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:设备
 * @Author: yedong
 * @Date: 2020/3/11 18:37
 * @Modified by:
 */
@Data
public class ResourceInfo {
    String resourceIndexCode;
    String resourceType = "acsDevice";
    List<Integer> channelNos = new ArrayList<>();

    public ResourceInfo() {
        this.channelNos.add(1);
    }
}
