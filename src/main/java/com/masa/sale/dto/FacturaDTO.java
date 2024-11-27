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
    private Integer id;
    private String date;
    private String status;
    private String total;
    private String comments;
    private CustomerDTO customer;
    private List<ExtItemDTO> items;

}
