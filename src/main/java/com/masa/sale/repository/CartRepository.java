package com.masa.sale.repository;

import com.masa.sale.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByIdAndEnabled(Long cartId, Boolean enabled);
}
