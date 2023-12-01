package com.example.proccess.upload.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Services {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    private Long code;
    private BigDecimal price;
    private String description;
    private BigDecimal qty;
    private BigDecimal measure;
    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;
    private Long category;
    private Boolean isActive;







    public  static  Services buildService( ServiceType serviceType, Dto dto) {
        return Services.builder()
                .code(dto.getCode())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .qty(dto.getQty())
                .measure(dto.getMeasure())
                .serviceType(serviceType)
                .category(dto.getCategory())
                .isActive(true)
                .build();
    }
}
