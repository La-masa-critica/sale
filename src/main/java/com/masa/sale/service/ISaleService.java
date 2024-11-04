package com.masa.sale.service;

import com.masa.sale.model.Sale;

import java.util.List;
import java.util.Optional;

public interface ISaleService {
    Optional<Sale> create(Long cartId);
    Optional<Sale> find(Long saleId);
    Optional<Sale> confirm(Long saleId);
    List<Sale> findAllByProfileId(Long profileId);
    Optional<Sale> cancel(Long saleId);
}
