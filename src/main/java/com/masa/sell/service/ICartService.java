package com.masa.sell.service;

import com.masa.sell.DTO.CartItemDTO;
import com.masa.sell.model.Cart;
import com.masa.sell.model.CartItem;

import java.util.Optional;

public interface ICartService {
    Optional<CartItem> addCartItem(CartItemDTO cartItem);

    Optional<Cart> getCart(Long profileId);
    Optional<Cart> updateCartItem(Long profileId, Long itemId, Integer quantity);
    Optional<Cart> deleteCartItem(Long profileId, Long itemId);
    void clearCart(Long profileId);
}
