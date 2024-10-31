package com.masa.sell.repository;

import com.masa.sell.model.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SaleDetailsRepository extends JpaRepository<SaleDetails, Long> {
    Set<SaleDetails> findBySaleId(Long saleId);
    void deleteBySaleId(Long saleId);
}
