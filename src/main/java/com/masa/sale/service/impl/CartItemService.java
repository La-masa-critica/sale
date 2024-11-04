package com.masa.sale.service.impl;

import com.masa.sale.model.Cart;
import com.masa.sale.model.CartItem;
import com.masa.sale.model.CartItemId;
import com.masa.sale.repository.CartItemRepository;
import com.masa.sale.service.ICartItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartItemService implements ICartItemService {
    private CartItemRepository cartItemRepository;

    @Override
    public Optional<CartItem> findById(Long cartId, Long itemId) {
        System.out.println("CartItemService.findById");
        System.out.println("cartId = " + cartId);
        System.out.println("itemId = " + itemId);
        return cartItemRepository.findById(
                CartItemId.builder()
                        .itemId(itemId)
                        .cartId(cartId)
                        .build()
        );
    }
    @Override
    public Optional<CartItem> addQuantity(Long cartId, Long itemId, Integer quantity) {
        System.out.println("CartItemService.addQuantity");
        CartItem cartItem = findById(cartId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (cartItem.getQuantity() + quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return updateCartItemQuantity(cartId, itemId, cartItem.getQuantity() + quantity);
    }

    @Override
    public Optional<CartItem> updateCartItemQuantity(Long cartId, Long itemId, Integer quantity) {
        System.out.println("CartItemService.updateCartItemQuantity");
        return findById(cartId, itemId)
                .map(cartItem -> cartItem.setQuantity(quantity))
                .map(this::saveCartItem);
    }

    @Override
    public CartItem createCartItem(Cart cart, Long itemId, Integer quantity) {
        System.out.println("CartItemService.createCartItem");
        if (cart.getId() == null || itemId == null) {
            throw new IllegalArgumentException("Cart ID or Item ID must not be null.");
        }
        return cartItemRepository.save(CartItem.builder()
                        .id(CartItemId.builder().cartId(cart.getId()).itemId(itemId).build())
                        .quantity(quantity)
                        .cart(cart)
                        .itemId(itemId)
                        .cartId(cart.getId())
                        .build());
    }

    @Override
    public Boolean exists(Long cartId, Long itemId) {
        System.out.println("CartItemService.exists");
        return cartItemRepository.existsById(CartItemId.builder().cartId(cartId).itemId(itemId).build());
    }

    @Transactional
    @Override
    public void delete(CartItem cartItem){
        System.out.println("CartItemService.delete");
        cartItemRepository.deleteByCartIdAndItemId(cartItem.getCartId(), cartItem.getItemId());
    }

    @Transactional
    @Override
    public void delete(Long cartId, Long itemId){
        System.out.println("CartItemService.delete");
        cartItemRepository.deleteByCartIdAndItemId(cartId, itemId);
    }

    @Override
    public void delteAll(Set<CartItem> cartItems){
        System.out.println("CartItemService.deleteAll");
        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    private CartItem saveCartItem(CartItem cartItem) {
        System.out.println("cartItem = " + cartItem);
        return cartItemRepository.save(cartItem);
    }

    @Autowired
    public void setCartItemRepository(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }
}
