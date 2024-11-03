package com.masa.sale.repository;

import com.masa.sale.model.CartItem;
import com.masa.sale.model.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findByCartId(Long cartId);

    @Modifying
    @Query("DELETE FROM cart_item c WHERE c.cartId = ?1 AND c.itemId = ?2")
    void deleteByCartIdAndItemId(Long cartId, Long itemId);

    @Modifying
    @Query("DELETE FROM cart_item c WHERE c.cartId = ?1")
    void deleteAllByCartId(Long cartId);
}
