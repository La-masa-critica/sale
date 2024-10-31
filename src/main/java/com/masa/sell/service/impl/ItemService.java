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
    public Optional<ItemDTO> getItem(Long itemId) {
        return Optional.ofNullable(itemClient.getItem(itemId));
    }

    @Override
    public Boolean itemExists(Long itemId) {
        return getItem(itemId).isPresent();
    }

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


