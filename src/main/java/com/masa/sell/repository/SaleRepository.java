package com.masa.sell.repository;

import com.masa.sell.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByProfileId(Long profileId);
    void deleteByProfileId(Long profileId);
}
