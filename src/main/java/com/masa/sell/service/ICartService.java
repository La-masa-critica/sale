package com.masa.sell.service;

import com.masa.sell.model.Cart;

import java.util.Optional;

public interface ICartService {
    Optional<Cart> addCartItem(Long profileId, Long itemId, Integer quantity);
    Optional<Cart> getCart(Long profileId);
    Optional<Cart> updateCartItem(Long profileId, Long itemId, Integer quantity);
    Optional<Cart> deleteCartItem(Long profileId, Long itemId);
    void clearCart(Long profileId);
}
