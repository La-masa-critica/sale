package com.masa.sell.service;

import com.masa.sell.model.Item;

import java.util.Optional;

public interface IItemService {
    Optional<Item> getItem(Long itemId);
    Optional<Item> createItem(Item item);
    Boolean existsByName(String name);
    Boolean existsById(Long itemId);
    void deleteItem(Long itemId);
}
