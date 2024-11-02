package com.masa.sell.controller;

import com.masa.sell.DTO.CartDTO;
import com.masa.sell.DTO.CartItemDTO;
import com.masa.sell.mapper.CartMapper;
import com.masa.sell.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private ICartService cartService;
    private CartMapper cartMapper;

    @GetMapping("/{profileId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long profileId) {
        return cartService.findById(profileId)
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.addCartItem(cartItemDTO)
                .flatMap(cartItem -> cartService.findById(cartItem.getId().getCartId()))
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> clearCart(@RequestParam Long profileId) {
        cartService.clearCart(profileId);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setCartService(ICartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

}
