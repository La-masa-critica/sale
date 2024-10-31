package com.masa.sell.service.impl;

import com.masa.sell.DTO.ItemDTO;
import com.masa.sell.client.ItemClient;
import com.masa.sell.model.*;
import com.masa.sell.repository.CartItemRepository;
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
    private final CartItemRepository cartItemRepository;

    public SaleService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    @Override
    public Optional<Sale> createSale(Long cartId) {
        Cart cart = cartService.getCart(cartId).orElseThrow(() -> new IllegalArgumentException("Cart does not exist"));
        Sale sale = saleRepository.save(newSale(cartId));
        // Add a log in the console with the sale ID
        Set<SaleDetails> saleDetails = createSaleDetails(sale, cart.getCartItems());
        BigDecimal total = calculateTotal(saleDetails);
        sale.setTotal(total).setSaleDetails(saleDetails);
        return Optional.of(saleRepository.save(sale));
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

    private Set<SaleDetails> createSaleDetails(Sale sale, Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> SaleDetails.builder()
                        .itemId(item.getItemId())
                        .sale(sale)
                        .quantity(item.getQuantity())
                        .price(itemService.getItem(item.getItemId()).map(ItemDTO::getPrice).orElse(BigDecimal.ZERO))
                        .build())
                .map(saleDetailsRepository::save)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Sale> getSale(Long saleId) {
        return saleRepository.findById(saleId);
    }

    private Optional<Sale> updateSale(Long saleId, String status) {
        if (!saleRepository.existsById(saleId) || (!status.equals("PAID") && !status.equals("CANCELLED"))) {
            return Optional.empty();
        }
        return getSale(saleId)
                .map(sale -> {
                    sale.setStatus(status);
                    return saleRepository.save(sale);
                });
    }

    @Override
    public Boolean cancelSale(Long saleId) {
        Sale sale = getSale(saleId).orElseThrow(() -> new IllegalArgumentException("Sale does not exist"));
        Cart cart = cartService.getCart(sale.getProfileId()).orElseThrow(() -> new IllegalArgumentException("Cart does not exist"));
        cart.getCartItems().forEach(cartItem -> itemService.updateItem(cartItem.getItemId(), cartItem.getQuantity()));
        return updateSale(saleId, "CANCELLED").isPresent();
    }

    @Override
    public Optional<Sale> confirmSale(Long saleId) {
        Sale sale = getSale(saleId).orElseThrow(() -> new IllegalArgumentException("Sale does not exist"));
        Cart cart = cartService.getCart(sale.getProfileId()).orElseThrow(() -> new IllegalArgumentException("Cart does not exist"));
        cartItemRepository.deleteAll(cart.getCartItems());
        return updateSale(saleId, "CONFIRMED");
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
