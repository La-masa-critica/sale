package com.masa.sell.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CartItemId implements Serializable {
    private Long cartId;
    private Long itemId;

    public CartItemId(Long cartId, Long itemId) {
        this.cartId = cartId;
        this.itemId = itemId;
    }

    public CartItemId() {
        this.cartId = 0L;
        this.itemId = 0L;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemId)) return false;
        CartItemId that = (CartItemId) o;
        return Objects.equals(getCartId(), that.getCartId()) &&
                Objects.equals(getItemId(), that.getItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCartId(), getItemId());
    }

}
