package com.masa.sell.service.impl;

import com.masa.sell.DTO.ItemDTO;
import com.masa.sell.exeptions.InventoryException;
import com.masa.sell.exeptions.ResourceNotFoundException;
import com.masa.sell.exeptions.SaleProcessingException;
import com.masa.sell.model.*;
import com.masa.sell.repository.CartItemRepository;
import com.masa.sell.repository.SaleDetailsRepository;
import com.masa.sell.repository.SaleRepository;
import com.masa.sell.service.ISaleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService implements ISaleService {
    private SaleRepository saleRepository;
    private CartService cartService;
    private SaleDetailsRepository saleDetailsRepository;
    private ItemService itemService;
    private CartItemRepository cartItemRepository;

    @Transactional
    @Override
    public Optional<Sale> create(Long cartId) {
        return cartService.findById(cartId)
                .map(this::processCartToSale);
    }

    private Sale processCartToSale(Cart cart) {
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

    private Sale completeSale(Sale sale, Set<CartItem> items) {
        Set<SaleDetails> saleDetails = createSaleDetails(sale, items);
        return sale.toBuilder()
                .total(calculateTotal(saleDetails))
                .saleDetails(saleDetails)
                .build();
    }

    private Set<SaleDetails> createSaleDetails(Sale sale, Set<CartItem> items) {
        return items.stream()
                .map(item -> createSaleDetail(sale, item))
                .map(saleDetailsRepository::save)
                .collect(Collectors.toUnmodifiableSet());
    }

    private SaleDetails createSaleDetail(Sale sale, CartItem item) {
        BigDecimal price = itemService.findById(item.getItemId())
                .map(ItemDTO::getPrice)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + item.getItemId()));

        return SaleDetails.builder()
                .itemId(item.getItemId())
                .sale(sale)
                .quantity(item.getQuantity())
                .price(price)
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

    @Transactional
    @Override
    public Optional<Sale> cancel(Long id) {
        return find(id)
                .map(sale -> executeCancellation(sale, getCart(sale.getProfileId())));
    }

    @Transactional
    @Override
    public Optional<Sale> confirm(Long id) {
        return find(id)
                .map(sale -> executeConfirmation(sale, getCart(sale.getProfileId())));
    }

    private Cart getCart(Long profileId) {
        return cartService.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + profileId));
    }

    private Sale executeCancellation(Sale sale, Cart cart) {
        restoreItems(cart.getCartItems());
        return transition(sale, SaleStatus.CANCELLED)
                .orElseThrow(() -> new SaleProcessingException("Failed to cancel sale: " + sale.getId()));
    }

    private Sale executeConfirmation(Sale sale, Cart cart) {
        removeCartItems(cart);
        return transition(sale, SaleStatus.COMPLETED)
                .orElseThrow(() -> new SaleProcessingException("Failed to confirm sale: " + sale.getId()));
    }

    private void restoreItems(Set<CartItem> items) {
        items.forEach(item ->
                itemService.incrementStock(item.getItemId(), item.getQuantity())
                        .orElseThrow(() -> new InventoryException("Failed to restore item: " + item.getItemId()))
        );
    }

    private void removeCartItems(Cart cart) {
        Optional.of(cart)
                .map(Cart::getCartItems)
                .ifPresent(cartItemRepository::deleteAll);
    }

    @Autowired
    public void setSaleRepository(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Autowired
    public void setCartItemRepository(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
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
