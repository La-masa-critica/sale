package com.masa.sale.repository;

import com.masa.sale.model.CartItem;
import com.masa.sale.model.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findByCartId(Long cartId);
    void deleteAllByCartId(Long cartId);
    void deleteByCartIdAndItemId(Long cartId, Long itemId);
}
