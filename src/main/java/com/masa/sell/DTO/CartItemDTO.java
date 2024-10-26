package com.masa.sell.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartId;
    private Long itemId;
    private Integer quantity;
}
