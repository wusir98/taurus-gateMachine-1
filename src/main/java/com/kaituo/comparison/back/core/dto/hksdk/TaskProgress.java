package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/11 20:11
 * @Modified by:
 */
@Data
public class TaskProgress {
    private String tagId;
    private String taskId;
    private Integer taskType;
    private Integer taskOptType;
    private String startTime;
    private Integer totalPercent;
    private Integer leftTime;
    private Integer consumeTime;
    private boolean isDownloadFinished;
    private List<ResourceDownloadProgress> resourceDownloadProgress;
}
