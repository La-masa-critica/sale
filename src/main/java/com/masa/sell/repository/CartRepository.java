package com.masa.sell.repository;

import com.masa.sell.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByProfileIdAndEnabled(Long profileId, Boolean enabled);
}
