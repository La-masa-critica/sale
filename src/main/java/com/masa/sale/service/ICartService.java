package com.masa.sale.service;

import com.masa.sale.model.Cart;
import com.masa.sale.model.CartItem;
import com.masa.sale.model.Sale;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;

public interface ICartService {
    Optional<CartItem> addCartItem(CartItem cartItem);
    Cart find(Long profileId);
    Optional<Cart> deleteCartItem(Long profileId, Long itemId);

    @Transactional
    Optional<Cart> findByCartItem(CartItem cartItem);

    void restoreItems(Set<CartItem> items);

    Optional<Cart> restoreCart(Sale sale);

    void clearCart(Long profileId);
    @Transactional
    Boolean cartExists(Long profileId);
}
