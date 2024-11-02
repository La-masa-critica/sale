package com.masa.sale.controller;

import com.masa.sale.dto.CartDTO;
import com.masa.sale.dto.CartItemDTO;
import com.masa.sale.mapper.CartItemMapper;
import com.masa.sale.mapper.CartMapper;
import com.masa.sale.service.ICartItemService;
import com.masa.sale.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private ICartService cartService;
    private CartMapper cartMapper;
    private ICartItemService cartItemService;
    private CartItemMapper cartItemMapper;

    @GetMapping("/{profileId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long profileId) {
        return Optional.ofNullable(cartService.find(profileId))
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.addCartItem(cartItemDTO)
                .flatMap(cartItem -> Optional.ofNullable(cartService.find(cartItem.getId().getCartId())))
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CartDTO> deleteCartItem(@RequestParam Long profileId, @RequestParam Long itemId) {
        return cartService.deleteCartItem(profileId, itemId)
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(@RequestParam Long profileId, @RequestParam Long itemId, @RequestParam Integer quantity) {
        return cartItemService.updateCartItemQuantity(profileId, itemId, quantity)
                .map(cartItemMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    public void setICartItemService(ICartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Autowired
    public void setCartService(ICartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Autowired
    public void setCartItemMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }
}
