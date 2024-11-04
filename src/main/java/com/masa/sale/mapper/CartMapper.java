package com.masa.sale.mapper;

import com.masa.sale.dto.CartDTO;
import com.masa.sale.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    CartDTO toDTO(Cart cart);
    Cart toEntity(CartDTO cartDTO);
}
