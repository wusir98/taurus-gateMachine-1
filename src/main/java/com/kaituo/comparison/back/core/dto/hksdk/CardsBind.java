package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/11 17:42
 * @Modified by:
 */
@Data
public class CardsBind {
    private String startDate;
    private String endDate;
    private List<CardInfo> cardList;
}
