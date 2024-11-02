package com.masa.sell.service;

import com.masa.sell.DTO.ItemDTO;
import java.util.Optional;

public interface IItemService {
    Optional<ItemDTO> findById(Long itemId);
    Boolean itemExists(Long itemId);
    Boolean updateItem(Long itemId, Integer quantity);
    Optional<ItemDTO> incrementStock(Long itemId, int quantity);
    Optional<ItemDTO> decrementStock(Long itemId, int quantity);
}
