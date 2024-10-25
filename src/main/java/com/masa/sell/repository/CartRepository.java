package com.masa.sell.repository;

import com.masa.sell.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByProfileId(Long profileId);
    Optional<Cart> findByProfileIdAndEnabled(Long profileId, Boolean enabled);
}
