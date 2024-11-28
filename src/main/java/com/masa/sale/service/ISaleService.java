package com.masa.sale.service;

import com.masa.sale.dto.FacturaDTO;
import com.masa.sale.model.Cart;
import com.masa.sale.model.Sale;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ISaleService {
    Optional<Sale> create(Long cartId);

    List<FacturaDTO> obtenerFactura();

    Optional<Sale> find(Long saleId);

    @Transactional
    Optional<Cart> failSale(Long saleId);

    Optional<Sale> confirm(Long saleId);
    List<Sale> findAllByProfileId(Long profileId);
    Optional<Sale> cancel(Long saleId);
}
