package com.masa.sale.service.impl;

import com.masa.sale.exeptions.InventoryException;
import com.masa.sale.model.Cart;
import com.masa.sale.model.CartItem;
import com.masa.sale.repository.CartRepository;
import com.masa.sale.service.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CartService implements ICartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ItemService itemService;

    @Override
    @Transactional
    public Optional<CartItem> addCartItem(CartItem cartItem) {
        validateCartItem(cartItem);

        Long profileId = cartItem.getCartId();
        Long itemId = cartItem.getItemId();
        Integer quantity = cartItem.getQuantity();
        Cart cart = cartExists(profileId) ? find(profileId) : createNewCart(profileId);

        return cartItemService.exists(cart.getId(), itemId)
                ? cartItemService.addQuantity(cart.getId(), itemId, quantity)
                : Optional.of(cartItemService.createCartItem(cart, itemId, quantity));
    }

    @Transactional
    @Override
    public Optional<Cart> deleteCartItem(Long cartId, Long itemId) {
        cartItemService.delete(cartId, itemId);
        return Optional.of(find(cartId));
    }


    @Override
    @Transactional
    public Cart find(Long profileId) {
        if (!profileExists(profileId)) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        return cartRepository.findById(profileId).orElse(createNewCart(profileId));
    }

    @Transactional
    @Override
    public Optional<Cart> findByCartItem(CartItem cartItem){
        return Optional.ofNullable(cartRepository.findById(cartItem.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart does not exist")));
    }

    @Transactional
    public Set<CartItem> getCartItems(Long cartId){
        return find(cartId).getCartItems();
    }

    @Override
    public void restoreItems(Set<CartItem> items) {
        items.forEach(item ->
                itemService.incrementStock(item.getItemId(), item.getQuantity())
                        .orElseThrow(() -> new InventoryException("Failed to restore item: " + item.getItemId()))
        );
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        cartItemService.findByCartId(cartId).forEach(cartItemService::delete);
    }

    @Override
    public Boolean cartExists(Long profileId) {
        return cartRepository.existsById(profileId);
    }

    private void validateCartItem(CartItem cartItem) {
        if (!profileExists(cartItem.getCartId())) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        if (cartItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!itemService.itemExists(cartItem.getItemId())) {
            throw new IllegalArgumentException("Item does not exist");
        }
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

    @Autowired
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Autowired
    public void setCartItemService(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
