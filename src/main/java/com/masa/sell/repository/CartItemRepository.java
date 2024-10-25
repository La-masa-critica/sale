package com.masa.sell.repository;

import com.masa.sell.model.CartItem;
import com.masa.sell.model.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findByCartId(Long cartId);
    void deleteByCartId(Long cartId);
    void deleteByCartIdAndItemId(Long cartId, Long itemId);
}
