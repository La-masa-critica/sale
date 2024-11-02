package com.masa.sale.mapper;

import com.masa.sale.dto.CartItemDTO;
import com.masa.sale.model.CartItem;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDTO toDTO(Long cartId, Long itemId, Integer quantity);
    CartItemDTO toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);
}
