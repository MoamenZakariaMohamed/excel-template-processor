package com.example.proccess.upload.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Integer success;
    private Integer fail;
    private Integer total;
}
