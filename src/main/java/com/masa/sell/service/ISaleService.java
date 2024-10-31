package com.masa.sell.service;

import com.masa.sell.model.Sale;

import java.util.Optional;

public interface ISaleService {
    Optional<Sale> createSale(Long cartId);
    Optional<Sale> getSale(Long saleId);
    Optional<Sale> confirmSale(Long saleId);
    Boolean cancelSale(Long saleId);
}
