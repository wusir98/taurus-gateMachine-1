package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/11 17:34
 * @Modified by:
 */
@Data
public class CardInfo {
    private String cardNo;
    private String personId;
    private String orgIndexCode = "1239017947124";
    /**
     * 卡片类型，默认是1：IC卡
     * 1：IC卡
     * 2：CPU卡
     * 3：远距离卡
     * 4：MIFARE卡
     */
    private Integer cardType = 1;
}
