package com.masa.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    private Long id;
    private String date;
    private String status;
    private Double total;
    private String comments;
    private CustomerDTO customer;
    private List<ExtItemDTO> items;

}
