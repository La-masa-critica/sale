package com.masa.sell.service.impl;

import com.masa.sell.DTO.CartItemDTO;
import com.masa.sell.model.Cart;
import com.masa.sell.model.CartItem;
import com.masa.sell.model.CartItemId;
import com.masa.sell.repository.CartItemRepository;
import com.masa.sell.repository.CartRepository;
import com.masa.sell.service.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements ICartService {
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Optional<CartItem> addCartItem(CartItemDTO cartItem) {
        if (!profileExists(cartItem.getCartId())) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        Long profileId = cartItem.getCartId();
        Long itemId = cartItem.getItemId();
        Integer quantity = cartItem.getQuantity();
        Cart cart = getCart(profileId).orElseGet(() -> createNewCart(profileId));
        return findCartItem(cart.getId(), itemId)
                .map(cartItem1 -> {
                    cartItem1.setQuantity(cartItem1.getQuantity() + quantity);
                    return Optional.of(cartItemRepository.save(cartItem1));
                })
                .orElseGet(() -> Optional.of(createCartItem(cart, itemId, quantity)));
    }

    private CartItem createCartItem(Cart cart, Long itemId, Integer quantity) {
        return cartItemRepository.save(
                CartItem.builder()
                        .id(CartItemId.builder()
                                .cartId(cart.getId())
                                .itemId(itemId)
                                .build())
                        .quantity(quantity)
                        .cart(cart)
                        .build()
        );
    }

    @Override
    @Transactional
    public Optional<Cart> getCart(Long profileId) {
        return cartRepository.findById(profileId);
    }

    private Cart createNewCart(Long profileId) {
        return cartRepository.save(
                Cart.builder()
                        .id(profileId)
                        .enabled(true)
                        .build()
        );
    }

    private Boolean profileExists(Long profileId) {
        //TODO: Call  profile service to check if profile exists
        return true;
    }


    @Transactional
    protected Optional<CartItem> findCartItem(Long cartId, Long itemId) {
        return cartItemRepository.findById(CartItemId.builder()
                .cartId(cartId)
                .itemId(itemId)
                .build());
    }



    @Override
    public Optional<Cart> updateCartItem(Long profileId, Long itemId, Integer quantity) {
        return Optional.empty();
    }

    @Override
    public Optional<Cart> deleteCartItem(Long profileId, Long itemId) {
        return Optional.empty();
    }

    @Override
    public void clearCart(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    @Autowired
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Autowired
    public void setCartItemRepository(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }
}
