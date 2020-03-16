package com.kaituo.comparison.back.core.dto.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventQueryDTO implements Serializable {
    private String startTime;
    private String endTime;
    private int pageNo;
    private int pageSize;
    private String sort="eventTime";
    private String order="asc";
}
