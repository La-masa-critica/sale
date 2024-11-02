package com.masa.sale.service;

import com.masa.sale.dto.ItemDTO;
import com.masa.sale.model.CartItem;

import java.util.Optional;
import java.util.Set;

public interface IItemService {
    Optional<ItemDTO> findById(Long itemId);
    Boolean itemExists(Long itemId);
    Optional<ItemDTO> incrementStock(Long itemId, int quantity);
    Optional<ItemDTO> decrementStock(Long itemId, int quantity);
    void restoreItems(Set<CartItem> items);
}
