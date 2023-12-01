package com.example.proccess.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Dto {
    private Long code;
    private BigDecimal price;
    private String description;
    private BigDecimal qty;
    private BigDecimal measure;
    private Long serviceType;
    private Long category;
    private Boolean isActive;
}
