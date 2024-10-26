package com.masa.sell.service.impl;

import com.masa.sell.model.Item;
import com.masa.sell.repository.ItemRepository;
import com.masa.sell.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements IItemService {
    private ItemRepository itemRepository;


    @Override
    public Optional<Item> getItem(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public Optional<Item> createItem(Item item) {
        return Optional.of(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Boolean existsByName(String name) {
        return itemRepository.findByName(name).isPresent();
    }

    @Override
    public Boolean existsById(Long itemId) {
        return itemRepository.findById(itemId).isPresent();
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findByEnabledTrue();
    }

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

}


