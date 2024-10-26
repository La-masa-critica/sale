package com.masa.sell.service;

import com.masa.sell.model.Cart;
import com.masa.sell.model.Sale;

import java.util.Optional;

public interface ISaleService {
    Optional<Sale> createSale(Cart cart);
    Optional<Sale> getSale(Long saleId);
    Optional<Sale> updateSale(Sale sale);
    String getSaleStatus(Long saleId);
}
