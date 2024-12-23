package com.masa.sale.service.impl;

import com.masa.sale.client.IFacturaExterna;
import com.masa.sale.dto.FacturaDTO;
import com.masa.sale.dto.ItemDTO;
import com.masa.sale.exeptions.InventoryException;
import com.masa.sale.exeptions.ResourceNotFoundException;
import com.masa.sale.exeptions.SaleProcessingException;
import com.masa.sale.mapper.FacturaMapper;
import com.masa.sale.model.*;
import com.masa.sale.repository.SaleDetailsRepository;
import com.masa.sale.repository.SaleRepository;
import com.masa.sale.service.ISaleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleService implements ISaleService {
    private final SaleRepository saleRepository;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final SaleDetailsRepository saleDetailsRepository;
    private final ItemService itemService;
    private final IFacturaExterna facturaExternaClient;

    @Transactional
    @Override
    public Optional<Sale> create(Long cartId) {
        return Optional.ofNullable(cartService.find(cartId))
                .map(this::processCartToSale);
    }

    @Override
    public List<FacturaDTO> obtenerFactura() {
        List<FacturasTot> ids = this.facturaExternaClient.obtenerFacturas("facturas", "2024-11-01", "2024-11-30", "1");
        List<FacturaExterna> listaFacturas= new ArrayList<>();

        for (FacturasTot facturaTot : ids) {
            listaFacturas.add(this.facturaExternaClient.getFacturaDetalle(facturaTot.getNumero_factura()));
        }
        return listaFacturas.stream().map(FacturaMapper.INSTANCE::facturaExternaToFacturaDTO)
                .collect(Collectors.toList());

    }


    private String convertirPrecio(String precio) {
        try {
            return String.valueOf(Double.parseDouble(precio)); // Convierte el String a Double y vuelve a String si se requiere
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error al convertir el precio unitario a Double: " + precio, e);
        }
    }


    @Transactional
    protected Sale processCartToSale(Cart cart) {
        return Optional.of(cart)
                .map(this::initializeSale)
                .map(saleRepository::save)
                .map(sale -> completeSale(sale, cart.getCartItems()))
                .map(saleRepository::save)
                .orElseThrow(() -> new SaleProcessingException("Failed to process sale"));
    }

    private Sale initializeSale(Cart cart) {
        return Sale.builder()
                .profileId(cart.getId())
                .status(SaleStatus.PENDING)
                .date(LocalDateTime.now())
                .build();
    }

    @Transactional
    protected Sale completeSale(Sale sale, Set<CartItem> items) {
        Set<SaleDetails> saleDetails = createSaleDetails(sale, items);
        items.forEach(cartItemService::delete);
        return sale.toBuilder()
                .total(calculateTotal(saleDetails))
                .saleDetails(saleDetails)
                .build();
    }

    private SaleDetails reserveItem(SaleDetails saleDetails) {
        return itemService.decrementStock(saleDetails.getItemId(), saleDetails.getQuantity())
                .map(item -> saleDetails)
                .orElseThrow(() -> new InventoryException("Failed to reserve item: " + saleDetails.getItemId()));
    }

    private Set<SaleDetails> createSaleDetails(Sale sale, Set<CartItem> items) {
        return items.stream()
                .map(item -> createSaleDetail(sale, item))
                .map(this::reserveItem)
                .map(saleDetailsRepository::save)
                .collect(Collectors.toUnmodifiableSet());
    }

    private SaleDetails createSaleDetail(Sale sale, CartItem item) {
        ItemDTO itemDTO = itemService.findById(item.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + item.getItemId()));

        return SaleDetails.builder()
                .itemId(item.getItemId())
                .itemName(itemDTO.getName())
                .sale(sale)
                .quantity(item.getQuantity())
                .price(itemDTO.getPrice())
                .build();
    }

    private BigDecimal calculateTotal(Set<SaleDetails> saleDetails) {
        return saleDetails.stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Optional<Sale> find(Long id) {
        return saleRepository.findById(id);
    }

    private Optional<Sale> transition(Sale sale, SaleStatus status) {
        return Optional.of(sale)
                .filter(s -> s.getStatus().canTransitionTo(status))
                .map(s -> s.toBuilder().status(status).build())
                .map(saleRepository::save);
    }

    @Override
    public List<Sale> findAllByProfileId(Long profileId) {
        return saleRepository.findByProfileId(profileId);
    }

    @Transactional
    @Override
    public Optional<Sale> cancel(Long id) {
        Sale sale = find(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return executeCancellation(sale);
    }

    @Transactional
    @Override
    public Optional<Cart> failSale(Long saleId) {
        Sale sale = find(saleId).orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + saleId));
        return executeFailure(sale)
                .flatMap(cartService::restoreCart);
    }

    private Optional<Sale> executeCancellation(Sale sale) {
        transition(sale, SaleStatus.CANCELLED)
                .orElseThrow(() -> new SaleProcessingException("Failed to cancel sale: " + sale.getId()));
        restoreItems(sale);
        return Optional.of(sale);
    }

    private Optional<Sale> executeFailure(Sale sale) {
        transition(sale, SaleStatus.FAILED)
                .orElseThrow(() -> new SaleProcessingException("Failed to fail sale: " + sale.getId()));
        restoreItems(sale);
        return Optional.of(sale);
    }

    private void restoreItem(SaleDetails saleDetails) {
        itemService.incrementStock(saleDetails.getItemId(), saleDetails.getQuantity())
                .orElseThrow(() -> new InventoryException("Failed to restore item: " + saleDetails.getItemId()));
    }

    private void restoreItems(Sale sale) {
        sale.getSaleDetails().forEach(this::restoreItem);
    }

    @Transactional
    @Override
    public Optional<Sale> confirm(Long saleId) {
        Sale sale = find(saleId).orElseThrow(() -> new RuntimeException("Sale not found with id: " + saleId));
        return Optional.of(executeConfirmation(sale));
    }

    private Sale executeConfirmation(Sale sale) {
        return transition(sale, SaleStatus.COMPLETED)
                .orElseThrow(() -> new SaleProcessingException("Failed to confirm sale: " + sale.getId()));
    }
}
