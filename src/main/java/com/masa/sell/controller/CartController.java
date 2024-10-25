package com.masa.sell.controller;

import com.masa.sell.DTO.CartItemDTO;
import com.masa.sell.model.Cart;
import com.masa.sell.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private ICartService cartService;

    @GetMapping("/{profileId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long profileId) {
        return cartService.getCart(profileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cart> addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.addCartItem(cartItemDTO.getCartId(), cartItemDTO.getItemId(), cartItemDTO.getQuantity())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }


    @Autowired
    public void setCartService(ICartService cartService) {
        this.cartService = cartService;
    }
}
