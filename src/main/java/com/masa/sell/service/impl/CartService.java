package com.masa.sell.service.impl;

import com.masa.sell.DTO.CartItemDTO;
import com.masa.sell.model.Cart;
import com.masa.sell.model.CartItem;
import com.masa.sell.model.CartItemId;
import com.masa.sell.repository.CartRepository;
import com.masa.sell.service.ICartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<Cart> addCartItem(Long cartId, Long itemId, Integer quantity) {
        Cart cart = new Cart();
        cart = cartRepository.findById(cartId).isPresent()
                ? cartRepository.findById(cartId).get()
                : cart;
        Optional<CartItem> cartItemOptional = findCartItem(cart, itemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = CartItem.builder()
                    .id(CartItemId.builder()
                            .cartId(cart.getId())
                            .itemId(itemId)
                            .build()
                    )
                    .itemId(itemId)
                    .quantity(quantity)
                    .build();
        }
        return Optional.of(cartRepository.save(cart));
    }

    private Optional<CartItem> findCartItem(Cart cart, Long itemId) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItemId().equals(itemId))
                .findFirst();
    }

    @Override
    public Optional<Cart> getCart(Long profileId) {
        return Optional.empty();
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
    public void clearCart(Long profileId) {

    }
}
