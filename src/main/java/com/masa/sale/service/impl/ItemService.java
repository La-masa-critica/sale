package com.masa.sell.service.impl;

import com.masa.sell.DTO.ItemDTO;
import com.masa.sell.client.ItemClient;
import com.masa.sell.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService implements IItemService {
    private ItemClient itemClient;

    @Override
    public Optional<ItemDTO> findById(Long itemId) {
        return Optional.ofNullable(itemClient.getItem(itemId));
    }

    @Override
    public Boolean itemExists(Long itemId) {
        return findById(itemId).isPresent();
    }

    @Override
    public Optional<ItemDTO> incrementStock(Long itemId, int quantity){
        try {
            return Optional.ofNullable(itemClient.updateItem(itemId, quantity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ItemDTO> decrementStock(Long itemId, int quantity);

    @Override
    public Boolean updateItem(Long itemId, Integer quantity) {
        try {
            return itemClient.updateItem(itemId, quantity);
        } catch (Exception e) {
            return false;
        }
    }

    @Autowired
    public void setItemClient(@Qualifier("com.masa.sell.client.ItemClient") ItemClient itemClient) {
        this.itemClient = itemClient;
    }

}


