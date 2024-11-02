package com.masa.sale.service;

import com.masa.sale.dto.CartItemDTO;
import com.masa.sale.model.Cart;
import com.masa.sale.model.CartItem;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;

public interface ICartService {
    Optional<CartItem> addCartItem(CartItemDTO cartItem);
    Cart find(Long profileId);
    Optional<Cart> deleteCartItem(Long profileId, Long itemId);
    void restoreItems(Set<CartItem> items);
    void clearCart(Long profileId);
    @Transactional
    Boolean cartExists(Long profileId);
}
