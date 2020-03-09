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


    //人员查询接口--获取组织下人员列表v2
    public static final String HK_ORGANIZATION_PERSON_PAGE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v2/person/orgIndexCode/personList";
    //人员查询接口--获取人员列表v2
    public static final String HK_PERSON_PAGE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/api/resource/v1/person/personList";
    //人员查询接口--查询人员列表
    public static final String HK_PERSON_LIST = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v2/person/advance/personList";
    //人员查询接口--根据人员唯一字段获取人员详细信息
    public static final String HK_PERSON_SINGLE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/condition/personInfo";
    //人员查询接口--增量获取人员数据
    public static final String HK_PERSON_TIME = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/personList/timeRange";

    //人员图片--提取人员图片
    public static final String HK_PICTURE = Configuration.getInstance().getValue(CommonConstant.HK_URL) + "/resource/v1/person/picture";
}
