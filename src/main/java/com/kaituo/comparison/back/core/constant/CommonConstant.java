package com.kaituo.comparison.back.core.constant;


import com.kaituo.comparison.back.core.utils.Configuration;

/**
 *
 */
public class CommonConstant {

    public static final String HK_URL = "HK_URL";
    public static final String KEY_SICKCITYINFODATA = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "largeScreen/sickCityInfoData";

}
