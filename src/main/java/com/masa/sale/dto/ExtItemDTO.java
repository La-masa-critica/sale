package com.masa.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtItemDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
}
