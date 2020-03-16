package com.kaituo.comparison.back.core.dto.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonQueryDTO implements Serializable {
    private String personId;
}
