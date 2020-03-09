package com.kaituo.comparison.back.core.constant;


import com.kaituo.comparison.back.core.utils.Configuration;

/**
 * 海康API
 */
public class CommonConstant {

    public static final String HK_URL = "HK_URL";
    //人员管理--添加人员
    public static final String HK_SINGLE_ADD_PEOPLE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/single/add";

    //人员管理--批量添加人员
    public static final String HK_BATCH_ADD_PEOPLE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/batch/add";

    //人员管理--修改人员
    public static final String HK_SINGLE_UPDATE_PEOPLE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/single/update";

    //人员管理--批量删除人员
    public static final String HK_BATCH_DELETE_PEOPLE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/batch/delete";

    //人员管理--批量添加人脸
    public static final String HK_SINGLE_ADD_FACE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/face/single/add";

    //人员管理--批量修改人脸
    public static final String HK_SINGLE_UPDATE_FACE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/face/single/update";

    //人员管理--批量删除人脸
    public static final String HK_SINGLE_DELETE_FACE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/face/single/delete";

}
