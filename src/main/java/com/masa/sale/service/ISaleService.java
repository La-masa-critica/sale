package com.masa.sale.service;

import com.masa.sale.model.CartItem;
import com.masa.sale.model.Sale;
import com.masa.sale.model.SaleDetails;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ISaleService {
    Optional<Sale> create(Long cartId);
    Optional<Sale> find(Long saleId);
    Optional<Sale> confirm(Long saleId);
    List<Sale> findAllByProfileId(Long profileId);
    Optional<Sale> cancel(Long saleId);
}
