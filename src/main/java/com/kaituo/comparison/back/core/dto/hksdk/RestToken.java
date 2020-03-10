package com.kaituo.comparison.back.core.dto.hksdk;

import lombok.Data;

@Data
public class RestToken {
    private String oldToken;
    private String newToken;
}
