package com.masa.sell.model;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItem{
    @EmbeddedId
    private CartItemId id = new CartItemId();

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long itemId;

    private Integer quantity;
}

