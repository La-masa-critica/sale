package com.masa.sale.controller;

import com.masa.sale.dto.CartDTO;
import com.masa.sale.dto.CartItemDTO;
import com.masa.sale.mapper.CartItemMapper;
import com.masa.sale.mapper.CartMapper;
import com.masa.sale.service.ICartItemService;
import com.masa.sale.service.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {
    private final ICartService cartService;
    private final CartMapper cartMapper;
    private final ICartItemService cartItemService;
    private final CartItemMapper cartItemMapper;

    @GetMapping()
    public ResponseEntity<CartDTO> getCart(
            @RequestHeader("X-User-ID") Long cartId
    ) {
        return Optional.ofNullable(cartService.find(cartId))
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addCartItem(
            @RequestHeader("X-User-ID") Long profileId,
            @RequestBody CartItemDTO cartItemDTO
    ) {
        return cartService.addCartItem(cartItemMapper.toEntity(cartItemDTO), profileId)
                .flatMap(cartService::findByCartItem)
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CartDTO> deleteCartItem(
            @RequestHeader("X-User-ID") Long profileId,
            @RequestParam Long itemId
    ) {
        return cartService.deleteCartItem(profileId, itemId)
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("X-User-ID") Long profileId
    ) {
        cartService.clearCart(profileId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(
            @RequestHeader("X-User-ID") Long profileId,
            @RequestParam Long itemId,
            @RequestParam Integer quantity
    ) {
        return cartItemService.updateCartItemQuantity(profileId, itemId, quantity)
                .map(cartItemMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
