package com.masa.sell.fallback;

import com.masa.sell.DTO.ItemDTO;
import com.masa.sell.client.ItemClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class ItemDTOFallback implements ItemClient {
    @Override
    public Set<ItemDTO> getEnabledItems() {
        return Collections.emptySet();
    }

    @Override
    public ItemDTO getItem(Long itemId) {
        return null;
    }

    @Override
    public Boolean updateItem(Long itemId, Integer quantity) {
        return false;
    }
}
