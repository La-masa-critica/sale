package com.masa.sale.service.impl;

import com.masa.sale.dto.ItemDTO;
import com.masa.sale.client.ItemClient;
import com.masa.sale.exeptions.InventoryException;
import com.masa.sale.model.CartItem;
import com.masa.sale.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
        return Optional.ofNullable(itemClient.incrementStock(itemId, quantity));
    }

    @Override
    public Optional<ItemDTO> decrementStock(Long itemId, int quantity){
        return Optional.ofNullable(itemClient.decrementStock(itemId, quantity));
    }

    @Override
    public void restoreItems(Set<CartItem> items) {
        items.forEach(item ->
                incrementStock(item.getItemId(), item.getQuantity())
                        .orElseThrow(() -> new InventoryException("Failed to restore item: " + item.getItemId()))
        );
    }

    @Autowired
    public void setItemClient(@Qualifier("com.masa.sale.client.ItemClient") ItemClient itemClient) {
        this.itemClient = itemClient;
    }

}


