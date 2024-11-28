package com.masa.sale.service.impl;

import com.masa.sale.exeptions.InventoryException;
import com.masa.sale.model.Cart;
import com.masa.sale.model.CartItem;
import com.masa.sale.model.Sale;
import com.masa.sale.repository.CartRepository;
import com.masa.sale.service.ICartService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ItemService itemService;

    @Override
    @Transactional
    public Optional<CartItem> addCartItem(CartItem cartItem, Long cartId) {
        validateCartItem(cartItem);
        Long itemId = cartItem.getItemId();
        Integer quantity = cartItem.getQuantity();
        Cart cart = cartExists(cartId) ? find(cartId) : createNewCart(cartId);

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
        return cartRepository.findById(profileId).orElseGet(() -> createNewCart(profileId));
    }

    @Transactional
    @Override
    public Optional<Cart> findByCartItem(CartItem cartItem){
        if (cartItem.getId().getCartId() == null) {
            throw new IllegalArgumentException("Cart does not exist");
        }
        return Optional.ofNullable(cartRepository.findById(cartItem.getId().getCartId())
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

    @Override
    @Transactional
    public Optional<Cart> restoreCart(Sale sale) {
        Set<CartItem> cartItems = sale.getSaleDetails().stream()
                .map(saleDetails -> CartItem.builder()
                        .itemId(saleDetails.getItemId())
                        .quantity(saleDetails.getQuantity())
                        .build()
                )
                .flatMap(cartItem -> this.addCartItem(cartItem, sale.getProfileId()).stream())
                .collect(Collectors.toUnmodifiableSet());
        return Optional.of(cartRepository.save(Cart.builder()
                .id(sale.getProfileId())
                .cartItems(cartItems)
                .enabled(true)
                .build()
        ));
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
        return true;
    }
}
