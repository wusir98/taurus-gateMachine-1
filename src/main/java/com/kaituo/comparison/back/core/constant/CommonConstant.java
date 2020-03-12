package com.kaituo.comparison.back.core.constant;


/**
 * 海康API
 */
public class CommonConstant {


    public static final String SUCCESS_CODE = "0";

    //人员管理--添加人员
    public static final String HK_SINGLE_ADD_PEOPLE = "/api/resource/v1/person/single/add";

    //人员管理--批量添加人员
    public static final String HK_BATCH_ADD_PEOPLE = "/api/resource/v1/person/batch/add";

    //人员管理--修改人员
    public static final String HK_SINGLE_UPDATE_PEOPLE = "/api/resource/v1/person/single/update";

    //人员管理--批量删除人员
    public static final String HK_BATCH_DELETE_PEOPLE = "/api/resource/v1/person/batch/delete";

    //人员管理--批量添加人脸
    public static final String HK_SINGLE_ADD_FACE = "/api/resource/v1/face/single/add";

    //人员管理--批量修改人脸
    public static final String HK_SINGLE_UPDATE_FACE = "/api/resource/v1/face/single/update";

    //人员管理--批量删除人脸
    public static final String HK_SINGLE_DELETE_FACE = "/api/resource/v1/face/single/delete";


    //人员查询接口--获取组织下人员列表v2
    public static final String HK_ORGANIZATION_PERSON_PAGE = "/api/resource/v2/person/orgIndexCode/personList";
    //人员查询接口--获取人员列表v2
    public static final String HK_PERSON_PAGE = "/api/api/resource/v1/person/personList";
    //人员查询接口--查询人员列表
    public static final String HK_PERSON_LIST = "/api/resource/v2/person/advance/personList";
    //人员查询接口--根据人员唯一字段获取人员详细信息
    public static final String HK_PERSON_SINGLE = "/api/resource/v1/person/condition/personInfo";
    //人员查询接口--增量获取人员数据
    public static final String HK_PERSON_TIME = "/api/resource/v1/person/personList/timeRange";

    //人员图片--提取人员图片
    public static final String HK_PICTURE = "/api/resource/v1/person/picture";
    //人员卡片--批量开卡
    public static final String HK_CARD_BINDS = "/api/cis/v1/card/bindings";
    //一卡通权限管理--下发权限
    public static final String HK_AUTH_ADD = "/api/acps/v1/auth_config/add";
    //一卡通权限管理--创建下载任务
    public static final String HK_TASK_ADD = "/api/acps/v1/download/configuration/task/add";
    //一卡通权限管理--开始下载任务
    public static final String HK_TASK_START = "/api/acps/v1/authDownload/task/start";
    //一卡通权限管理--查询下载任务进度
    public static final String HK_TASK_PROGRESS = "/api/acps/v1/authDownload/task/progress";
}
