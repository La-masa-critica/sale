package com.masa.sell.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem{
    @EmbeddedId
    private CartItemId id = new CartItemId();

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Long itemId;

    private Integer quantity;
}

