package com.masa.sale.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long itemId;
    private Integer quantity;
}
