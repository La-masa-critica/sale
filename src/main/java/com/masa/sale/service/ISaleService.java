package com.masa.sale.service;

import com.masa.sale.dto.FacturaDTO;
import com.masa.sale.model.Sale;

import java.util.List;
import java.util.Optional;

public interface ISaleService {
    Optional<Sale> create(Long cartId);

    List<FacturaDTO> obtenerFactura();

    Optional<Sale> find(Long saleId);
    Optional<Sale> confirm(Long saleId);
    List<Sale> findAllByProfileId(Long profileId);
    Optional<Sale> cancel(Long saleId);
}
