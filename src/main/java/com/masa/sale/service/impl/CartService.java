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
    private ItemService itemService;

    @Override
    @Transactional
    public Optional<CartItem> addCartItem(CartItemDTO cartItem) {
        if (!profileExists(cartItem.getCartId())) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        Long profileId = cartItem.getCartId();
        Long itemId = cartItem.getItemId();
        Integer quantity = cartItem.getQuantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!itemService.itemExists(itemId)) {
            throw new IllegalArgumentException("Item does not exist");
        }
        System.out.println("Item exists");
        System.out.println("item id: " + itemId);
        if (!itemService.updateItem(itemId, -quantity)) {
            throw new IllegalArgumentException("Quantity is not enough");
        }

        Cart cart = findById(profileId).orElseGet(() -> createNewCart(profileId));
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
    public Optional<Cart> findById(Long profileId) {
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
    @Transactional
    public Optional<Cart> updateCartItem(Long profileId, Long itemId, Integer quantity) {
        Cart cart = findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for profile: " + profileId));

        CartItem cartItem = findCartItem(cart.getId(), itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not in cart"));

        int quantityDifference = quantity - cartItem.getQuantity();

        if (!itemService.updateItem(itemId, -quantityDifference)) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return Optional.of(cart);
    }

    @Transactional
    @Override
    public Optional<Cart> deleteCartItem(Long profileId, Long itemId) {
        Cart cart = findById(profileId).orElseThrow(() -> new IllegalArgumentException("Cart does not exist"));
        return findCartItem(cart.getId(), itemId)
                .map(cartItem -> {
                    itemService.updateItem(itemId, cartItem.getQuantity());
                    cartItemRepository.delete(cartItem);
                    return Optional.of(cart);
                })
                .orElse(Optional.empty());
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        Cart cart = findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart does not exist"));
        cart.getCartItems().forEach(cartItem -> itemService.updateItem(cartItem.getId().getItemId(), cartItem.getQuantity()));
        cartItemRepository.deleteAll(cart.getCartItems());
    }

    @Autowired
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Autowired
    public void setCartItemRepository(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
