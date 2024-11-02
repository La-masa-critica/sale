package com.masa.sell.mapper;

import com.masa.sell.DTO.CartDTO;
import com.masa.sell.DTO.CartItemDTO;
import com.masa.sell.model.Cart;
import com.masa.sell.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartDTO toDTO(Cart cart) {
        if (cart == null) return null;

        return CartDTO.builder()
                .id(cart.getId())
                .enabled(cart.getEnabled())
                .items(cart.getCartItems().stream()
                        .map(this::toCartItemDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    private CartItemDTO toCartItemDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .cartId(cartItem.getCartId())
                .itemId(cartItem.getItemId())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
