package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
    String resourceType;
    List<Integer> channelNos = new ArrayList<>();

/*    public ResourceInfo() {
        //this.channelNos.add(1);
       // this.channelNos.add(2);
    }*/

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if(this==obj){
            return true;
        }
        if(StringUtils.isEmpty(this.resourceIndexCode)){
            return false;
        }
        if(obj instanceof ResourceInfo){
            ResourceInfo resourceInfo=(ResourceInfo)obj;
            if(StringUtils.isEmpty(resourceInfo.resourceIndexCode)){
                return false;
            }
            if(resourceInfo.resourceIndexCode.equals(resourceInfo.resourceIndexCode)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (resourceIndexCode == null ? 0 : resourceIndexCode.hashCode());
        return result;
    }
}
