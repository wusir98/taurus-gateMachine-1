package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

/**
 * @Description:
 * @Author: yedong
 * @String: 2020/3/11 20:11
 * @Modified by:
 */
@Data
public class ResourceDownloadProgress {

    private ResourceInfo resourceInfo;
    private String startTime;
    private String updateTime;
    private String endTime;
    private Integer leftTime;
    private String errorCode;
    private Integer downloadResult;
    private Integer consumeTime;
    private String downloadStatus;
    private boolean isDownloadFinished;
    private Integer downloadPercent;
    private Integer totalPersonCount;
    private Integer downloadPersonCount;
    private Integer successedPersonCount;
    private Integer failedPersonCount;
}
