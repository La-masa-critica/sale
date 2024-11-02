package com.masa.sell.service;

import com.masa.sell.model.Sale;

import java.util.Optional;

public interface ISaleService {
    Optional<Sale> create(Long cartId);
    Optional<Sale> find(Long saleId);
    Optional<Sale> confirm(Long saleId);
    Optional<Sale> cancel(Long saleId);
}
