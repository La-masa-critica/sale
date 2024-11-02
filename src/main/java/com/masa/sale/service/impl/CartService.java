package com.masa.sale.service.impl;

import com.masa.sale.dto.CartItemDTO;
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
    public Optional<CartItem> addCartItem(CartItemDTO cartItemDTO) {
        validateCartItemDTO(cartItemDTO);

        Long profileId = cartItemDTO.getCartId();
        Long itemId = cartItemDTO.getItemId();
        Integer quantity = cartItemDTO.getQuantity();
        System.out.println("profileId: " + profileId);
        System.out.println("itemId: " + itemId);
        System.out.println("quantity: " + quantity);
        Cart cart = cartExists(profileId) ? find(profileId) : createNewCart(profileId);

        return cartItemService.exists(cart.getId(), itemId)
                ? cartItemService.addQuantity(cart.getId(), itemId, quantity)
                : Optional.of(cartItemService.createCartItem(cart, itemId, quantity));
    }

    @Transactional
    @Override
    public Optional<Cart> deleteCartItem(Long cartId, Long itemId) {
        // TODO: Fix deleteCartItem method. Actually, it is not working.
        cartItemService.delete(cartId, itemId);
        return Optional.of(find(cartId));
    }


    @Override
    @Transactional
    public Cart find(Long profileId) {
        return cartRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for profile: " + profileId));
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

    private void validateCartItemDTO(CartItemDTO cartItemDTO) {
        if (!profileExists(cartItemDTO.getCartId())) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        if (cartItemDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!itemService.itemExists(cartItemDTO.getItemId())) {
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
