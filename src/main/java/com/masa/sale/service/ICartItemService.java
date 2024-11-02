package com.masa.sale.service;

import com.masa.sale.model.Cart;
import com.masa.sale.model.CartItem;

import java.util.Optional;
import java.util.List;
import java.util.Set;

public interface ICartItemService {
    Optional<CartItem> findById(Long cartId, Long itemId);
    Optional<CartItem> addQuantity(Long cartId, Long itemId, Integer quantity);
    Optional<CartItem> updateCartItemQuantity(Long cartId, Long itemId, Integer quantity);
    CartItem createCartItem(Cart cart, Long itemId, Integer quantity);

    Boolean exists(Long cartId, Long itemId);

    void delete(CartItem cartItem);
    void delete(Long cartId, Long itemId);

    void delteAll(Set<CartItem> cartItems);

    List<CartItem> findByCartId(Long cartId);
}
