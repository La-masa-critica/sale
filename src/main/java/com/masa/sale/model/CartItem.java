package com.masa.sell.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItem{
    @Builder.Default
    @EmbeddedId
    private CartItemId id = new CartItemId();

    @JsonIgnore
    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "cart_id", insertable = false, updatable = false)
    private Long cartId;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long itemId;

    private Integer quantity;
}

