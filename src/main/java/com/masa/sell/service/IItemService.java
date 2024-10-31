package com.masa.sell.service;

import com.masa.sell.DTO.ItemDTO;
import java.util.Optional;

public interface IItemService {
    Optional<ItemDTO> getItem(Long itemId);
    Boolean itemExists(Long itemId);
    Boolean updateItem(Long itemId, Integer quantity);
}
