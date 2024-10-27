package com.masa.sell.service.impl;

import com.masa.sell.DTO.ItemDTO;
import com.masa.sell.client.ItemClient;
import com.masa.sell.model.*;
import com.masa.sell.repository.SaleDetailsRepository;
import com.masa.sell.repository.SaleRepository;
import com.masa.sell.service.ISaleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService implements ISaleService {
    private SaleRepository saleRepository;
    private CartService cartService;
    private SaleDetailsRepository saleDetailsRepository;
    private ItemService itemService;

    @Transactional
    @Override
    public Optional<Sale> createSale(Long cartId) {
        Cart cart = cartService.getCart(cartId).orElseThrow(() -> new IllegalArgumentException("Cart does not exist"));
        Sale sale = newSale(cartId);
        Set<SaleDetails> saleDetails = createSaleDetails(sale.getId(), cart.getCartItems());
        BigDecimal total = calculateTotal(saleDetails);
        return Optional.of(saleRepository.save(sale.setTotal(total).setSaleDetails(saleDetails)));
    }

    private Sale newSale(Long cartId) {
        return Sale.builder()
                .profileId(cartId)
                .comments("")
                .status("PENDING")
                .date(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    private BigDecimal calculateTotal(Set<SaleDetails> saleDetails) {
        return saleDetails.stream()
                .map(saleDetail -> saleDetail.getPrice().multiply(BigDecimal.valueOf(saleDetail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<SaleDetails> createSaleDetails(Long saleId, Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> SaleDetails.builder()
                        .itemId(item.getItemId())
                        .quantity(item.getQuantity())
                        .price(itemService.getItem(item.getItemId()).map(ItemDTO::getPrice).orElse(BigDecimal.ZERO))
                        .saleId(saleId)
                        .build())
                .map(saleDetailsRepository::save)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Sale> getSale(Long saleId) {
        return Optional.empty();
    }

    @Override
    public Optional<Sale> updateSale(Long saleId) {
        return Optional.empty();
    }

    @Autowired
    public void setSaleRepository(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setSaleDetailsRepository(SaleDetailsRepository saleDetailsRepository) {
        this.saleDetailsRepository = saleDetailsRepository;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
