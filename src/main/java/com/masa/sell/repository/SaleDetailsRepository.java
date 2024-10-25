package com.masa.sell.repository;

import com.masa.sell.model.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailsRepository extends JpaRepository<SaleDetails, Long> {
    List<SaleDetails> findBySaleId(Long saleId);
    void deleteBySaleId(Long saleId);
}
